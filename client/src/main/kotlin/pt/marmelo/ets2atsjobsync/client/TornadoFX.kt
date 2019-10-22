package pt.marmelo.ets2atsjobsync.client

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.Priority
import javafx.stage.Stage
import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.common.payload.JobPayload
import pt.marmelo.ets2atsjobsync.common.utils.JacksonUtils
import tornadofx.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

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
                        selectedToggleProperty().addListener { _, oldValue, newValue -> if (newValue == null) oldValue.isSelected = true }
                        togglebutton(Game.ETS2.name).apply { isSelected = true }
                        togglebutton(Game.ATS.name)
                        togglebutton("ProMods")
                    }
                    pane { hgrow = Priority.ALWAYS }
                    button("Refresh") {
                        alignment = Pos.CENTER_RIGHT
                    }.action {
                        controller.refresh()
                    }
                }
                hbox {
                    label("Settings Folder: ")
                    label(controller.gameDir)
                }
                /*hbox {
                    label("Save Format: ")
                    label("Text")
                }
                hbox {
                    label("Settings OK") {
                        textFill = Color.GREEN
                        style = "-fx-font-weight: bold"
                    }
                }*/
            }
        }
        titledpane("Select {GAME} Profile") {
            isCollapsible = false
            label("X {GAME} profile.")
            combobox<Profile>(controller.selectedProfile) {
                items = controller.profileList
            }
        }
        titledpane("Select Save") {
            isCollapsible = false
            label("X {GAME} save.")
            combobox<Save>(controller.selectedSave) {
                items = controller.saveList
            }
        }
        titledpane("DLCs") {
            isCollapsible = false
            vbox {
                hbox {
                    hgrow = Priority.ALWAYS
                    label("Also include jobs that require:")
                    pane { hgrow = Priority.ALWAYS }
                    hyperlink("Select all"){
                        alignment = Pos.CENTER_LEFT
                    }.action {

                    }
                }
                hbox {
                    label("WIP")
                }
            }
        }
        titledpane("Job List") {
            isCollapsible = false
            vbox {
                combobox<String>(controller.selectedJobList) {
                    items = controller.availableJobLists
                }
            }
        }
        hbox {
            button("Sync Jobs") {
                alignment = Pos.CENTER_RIGHT
            }.action {
                controller.syncStatus.value = "Syncing"
                runAsyncWithOverlay {
                    controller.sync()
                } ui {
                    controller.syncStatus.value = "Synced"
                }
            }
            label(controller.syncStatus)
            /*pane { hgrow = Priority.ALWAYS }
            button("Close") {
                alignment = Pos.CENTER_LEFT
            }*/
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

    val availableJobLists = mutableListOf<String>().observable()
    val selectedJobList = SimpleStringProperty()

    val syncStatus = SimpleStringProperty("Stopped")

    init {
        createJobDirs()
        updateData(Game.ETS2)
        updateJobLists(Game.ETS2.name)

        selectedGame.onChange {
            it ?: return@onChange
            updateData(gameFromString(it))
            updateJobLists(it)
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

    private fun createJobDirs() {
        listOf(Game.ETS2.name,
            Game.ATS.name,
            "ProMods")
            .forEach { File(it).mkdir() }
    }

    private fun updateJobLists(game: String) {
        availableJobLists.clear()
        val currentDirectory = Paths.get(game)
        Files.walk(currentDirectory, 1)
            .filter { f -> f != currentDirectory } // skip root
            .filter { f -> Files.isRegularFile(f) }
            .forEach{ f -> availableJobLists.add(f.toFile().name) }
        availableJobLists.sort()
        if (!availableJobLists.isEmpty())
            selectedJobList.value = availableJobLists[0]
    }

    fun sync() {
        val jobListPath = Paths.get(selectedGame.value).resolve(selectedJobList.value)
        val jobsList: List<JobPayload> = JacksonUtils.fromString(String(Files.readAllBytes(jobListPath)))
        selectedSave.value.replaceJobs(jobsList)
    }

    private fun gameFromString(game: String): Game =
        when(game) {
            Game.ETS2.name, "ProMods" -> Game.ETS2
            Game.ATS.name -> Game.ATS
            else -> Game.INVALID
        }

    fun refresh() {
        updateData(gameFromString(selectedGame.value))
        updateJobLists(selectedGame.value)
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

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        launch<MyApp>()
    }
}
