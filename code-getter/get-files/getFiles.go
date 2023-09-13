package getfiles

import (
	"os"
	"path/filepath"
	"sync"
)

func GetAllFiles(dir string) []string {
	channel := make(chan string)
	wg := &sync.WaitGroup{}
	wg.Add(1)
	go getAllFilesAuxiliary(channel, dir, wg, true)

	files := []string{}
	for file := range channel {
		files = append(files, file)
	}

	return files
}

func getAllFilesAuxiliary(channel chan string, dir string, wg *sync.WaitGroup, isRoot bool) {
	defer func() {
		if isRoot {
			// allows the first call to return, so i could read from the channel and don't block
			go func() {
				wg.Wait()
				close(channel)
			}()
		}
		wg.Done()
	}()

	dirEntry, err := os.ReadDir(dir)
	if err != nil {
		panic(err)
	}

	for _, f := range dirEntry {
		if f.IsDir() {
			nextDir := filepath.Join(dir, f.Name())
			wg.Add(1)
			go getAllFilesAuxiliary(channel, nextDir, wg, false)
		} else {
			channel <- filepath.Join(dir, f.Name())
		}
	}
}
