package pt.marmelo.ets2atsjobsync.client

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.stage.Stage
import pt.marmelo.ets2atsjobsync.common.Game
import tornadofx.*

class MyView : View("ETS2 ATS Job Sync") {
    private val controller: MyController by inject()

    override val root = form {
        titledpane("Game Settings") {
            isCollapsible = false
            vbox {
                hbox {
                    label("Game: ")
                    spacing = 10.0
                    togglegroup {
                        bind(controller.selectedGame)
                        togglebutton(Game.ETS2.name).apply { isSelected = true }
                        togglebutton(Game.ATS.name)
                        togglebutton("ProMods")
                    }
                }
                hbox {
                    label("Settings Folder: ")
                    label(controller.gameDir)
                }
                hbox {
                    label("Save Format: ")
                    label("Text")
                }
                hbox {
                    label("Settings OK") {
                        textFill = Color.GREEN
                        style = "-fx-font-weight: bold"
                    }
                }
            }
        }
        titledpane("Select {GAME} Profile") {
            isCollapsible = false
            label("X {GAME} profile.")
            combobox<Profile>(controller.selectedProfile) {
                //selectionModel.selectFirst()
                items = controller.profileList
            }
        }
        titledpane("Select Save") {
            isCollapsible = false
            label("X {GAME} save.")
            combobox<Save>(controller.selectedSave) {
                //selectionModel.selectFirst()
                items = controller.saveList
            }
        }
        titledpane("DLCs") {
            isCollapsible = false
            hbox {
                label("Also include jobs that require:")
                pane { hgrow = Priority.ALWAYS }
                hyperlink("Select all"){
                    alignment = Pos.CENTER_LEFT
                }.action {

                }
            }
        }
        titledpane("Job List") {
            isCollapsible = false
            combobox<String> {
                items = FXCollections.observableArrayList("1", "2")
            }
        }
        hbox {
            button("Sync Jobs") {
                alignment = Pos.CENTER_RIGHT
            }
            pane { hgrow = Priority.ALWAYS }
            button("Close") {
                alignment = Pos.CENTER_LEFT
            }
        }
    }
}

class MyController : Controller() {
    var gameUpdating = false
    val selectedGame = SimpleStringProperty(Game.ETS2.name)
    val gameDir = SimpleStringProperty(Game.ETS2.savePath.toString())
    private lateinit var gameInfo: Info
    var profileList = mutableListOf<Profile>().observable()
    val selectedProfile = SimpleObjectProperty<Profile>()
    var saveList = mutableListOf<Save>().observable()
    val selectedSave = SimpleObjectProperty<Save>()

    init {
        updateData(Game.ETS2)

        selectedGame.onChange {
            updateData(gameFromString(it!!))
        }

        selectedProfile.onChange {
            if (gameUpdating) return@onChange
            saveList.clear()
            saveList.addAll(it!!.saves)
            selectedSave.value = saveList[0]
        }

    }

    private fun updateData(game: Game) {
        gameUpdating = true
        gameDir.value = game.savePath.toString()
        gameInfo = Info(game)
        profileList.clear()
        profileList.addAll(gameInfo.profiles)
        selectedProfile.value = profileList[0]
        saveList.clear()
        saveList.addAll(gameInfo.profiles[0].saves)
        selectedSave.value = saveList[0]
        gameUpdating = false
    }

    private fun gameFromString(game: String): Game =
        when(game) {
            Game.ETS2.name, "ProMods" -> Game.ETS2
            Game.ATS.name -> Game.ATS
            else -> Game.INVALID
        }
}

class MyApp : App(MyView::class, Styles::class) {
    override fun start(stage: Stage) {
        stage.icons += Image("iconw_256.png")
        super.start(stage)
        stage.width = 500.0
        //stage.height = 650.0
        stage.isResizable = false
    }
}

class Styles : Stylesheet() {
    init {
        /*label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }*/
    }
}

fun main() {
    launch<MyApp>()
}