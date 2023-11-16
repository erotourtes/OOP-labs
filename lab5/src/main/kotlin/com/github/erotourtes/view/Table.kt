package com.github.erotourtes.view

import com.github.erotourtes.drawing.shape.Shape
import tornadofx.*
import kotlin.reflect.KProperty1

class Table : View("Table") {
    private val model by inject<CanvasModel>()
    private val editorsInfoModel by inject<EditorsInfoModel>()
    private val shapeModel: Shape.ShapeModel = Shape.ShapeModel(model.cc.emptyShape())

    override val root = borderpane {
        val data = model.sl.getObservableList()
        val editorHandler = model.eh

        top = button("Table of shapes") { action { println(data) } }

        center = tableview<Shape> {
            column("x1") {
                it.value.model.x1
                // TODO: WTF calls several times
            }

            items = data
            onUserSelect(1, editorHandler.editor::highlight)

            shapeModel.rebindOnChange(this) { item = it }
        }

        bottom = form {
            fieldset("Selected Shape") {
                field("x1: ") { textfield(shapeModel.x1) }
                field("x2: ") { textfield(shapeModel.x2) }
                field("y1: ") { textfield(shapeModel.y1) }
                field("y2: ") { textfield(shapeModel.y2) }
                button("Save") {
                    enableWhen(shapeModel.dirty)
                    action { shapeModel.commit() }
                }
                button("Reset") { action { shapeModel.rollback() } }
            }
        }
    }

}
/*
class Table : View("Table") {
    override val root = tableview {
//        readonlyColumn("Name", EditorInfo::name)
//        readonlyColumn("x1", )
//        readonlyColumn("x2", EditorInfo::tooltip)
//        readonlyColumn("y1", EditorInfo::tooltip)
//        readonlyColumn("y2", EditorInfo::tooltip)
//        column("Icon", EditorInfo::icon)
//        val list = find<CanvasModel>().sl
        val a = Person("Mike", 42)
        val list = listOf(
            a,
            Person("John", 42, a),
            Person("Jane", 36),
        )

        column("Name", Person::getNameProp).makeEditable()
        readonlyColumn("Age", Person::age)
        column<Person, String>("Parent name") {
            it.value.parrent?.getNameProp() ?: SimpleStringProperty()
        }.makeEditable()
//        column("Parent name", Person::getParentProp).cellFormat {
//            // doesn't update parent if changed trhough child
//            textProperty().bind(it?.getParentProp()?.value?.getNameProp() ?: SimpleStringProperty())
//        }

        column<Person, String>("Parent name") {
            it.value.getParentProp().select {
                if (it != null)
                    return@select it.getNameProp()
                else return@select SimpleStringProperty()
            }
        }

        items = list.toList().asObservable()
    }

    class Person(name: String, age: Int, parrent: Person? = null) {
//        private val nameProp = SimpleStringProperty(name)
//        fun getNameProp() = nameProp
//        var name by nameProp




        private val ageProp = SimpleIntegerProperty(age)
        var age by ageProp

        var parrentProp = SimpleObjectProperty(parrent)
        fun getParentProp() = parrentProp
        var parrent by parrentProp


//        var arg by property("arg")
//        fun getArgProp() = getProperty(Person::arg)
    }
}*/
