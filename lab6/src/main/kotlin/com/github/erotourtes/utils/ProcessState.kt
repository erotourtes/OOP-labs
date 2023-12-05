package com.github.erotourtes.utils

import com.github.erotourtes.utils.process_stream.ProcessReceiver
import com.github.erotourtes.utils.process_stream.ProcessSender

class ProcessState(
    private val process: Process,
    val sender: ProcessSender,
    receiver: ProcessReceiver,
) : Closable {
    val alive get() = isAlive && process.isAlive
    val ee = EventEmitter(receiver)

    private var isAlive: Boolean = true
    private var isDestroyedMsgReceived: Boolean = false
    private var isClosed: Boolean = false

    init {
        ee.subscribe(MessageType.ON_DESTROY) { isDestroyedMsgReceived = true }
    }

    override fun close() {
        if (isClosed) {
            Logger.log("ProcessState(close): process already closed", Logger.InfoType.WARNING)
            return
        }
        runCatching {
            sender.send(MessageType.DESTROY)
            // To see the logs
            while (!isDestroyedMsgReceived) Thread.sleep(PROCESS_UPDATE_TIME)
            Logger.log("process has been closed isAlive:${process.isAlive}")
            sender.close()
            ee.close()
            process.destroy()
            isAlive = false
            isClosed = true
        }.onFailure {
            Logger.log("ProcessState(close): ${it.message}", Logger.InfoType.ERROR)
        }
    }
}