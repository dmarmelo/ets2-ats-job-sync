package pt.marmelo.ets2atsjobsync.client.ui

import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import javafx.stage.Stage
import pt.marmelo.ets2atsjobsync.client.Profile
import pt.marmelo.ets2atsjobsync.client.Save
import pt.marmelo.ets2atsjobsync.common.Dlc
import pt.marmelo.ets2atsjobsync.common.Game
import tornadofx.*
import tornadofx.controlsfx.statusbar
import java.nio.file.Paths

class MyView : View("ETS2 ATS Job Sync") {
    private val controller: MyController by inject()

    override val root = borderpane {
        center = form {
            titledpane("Game Settings") {
                isCollapsible = false
                vbox {
                    hbox {
                        label("Game: ")
                        spacing = 10.0
                        togglegroup {
                            bind(controller.selectedGame)
                            selectedToggleProperty().addListener { _, oldValue, newValue ->
                                if (newValue == null) oldValue.isSelected = true
                            }
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
                    hbox {
                        label("Save Format: ")
                        label(controller.saveFormat)
                    }
                    hbox {
                        add(controller.saveFormatStatus)
                    }
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
                        hyperlink("Select all") {
                            alignment = Pos.CENTER_LEFT
                        }.action {
                            controller.selectAllDlcs()
                        }
                    }
                    hbox {
                        checkbox(Dlc.HIGH_POWER_CARGO.title) {
                            spacing = 20.0
                            userData = Dlc.HIGH_POWER_CARGO
                        }.apply { controller.dlcCheckBoxes.add(this) }
                        checkbox(Dlc.HEAVY_CARGO.title) {
                            userData = Dlc.HEAVY_CARGO
                        }.apply { controller.dlcCheckBoxes.add(this) }
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
                paddingTop = 10.0
                spacing = 20.0
                button("Sync Jobs").action {
                    controller.status.value = "Syncing..."
                    runAsyncWithOverlay {
                        controller.sync()
                    } ui {
                        controller.status.value = "Synced!"
                    }
                }
                button("Extract Jobs").action {
                    val filter = arrayOf(FileChooser.ExtensionFilter("JSON File (*.json)", "*.json"))
                    val fileToSave = chooseFile("Save to", filter, FileChooserMode.Save) {
                        initialDirectory = Paths.get(controller.selectedGame.value).toFile()
                    }
                    if (fileToSave.isNotEmpty()) {
                        controller.status.value = "Extracting..."
                        runAsyncWithOverlay {
                            controller.extract(fileToSave[0])
                        } ui {
                            controller.status.value = "Extracted!"
                        }
                    }
                }
                button("Clear Jobs").action {
                    controller.status.value = "Clearing..."
                    runAsyncWithOverlay {
                        controller.clearJobs()
                    } ui {
                        controller.status.value = "Cleared!"
                    }
                }
                /*pane { hgrow = Priority.ALWAYS }
                button("Close") {
                    alignment = Pos.CENTER_LEFT
                }*/
            }
        }
        bottom = statusbar(controller.status)
    }
}

class MyApp : App(MyView::class) {
    override fun start(stage: Stage) {
        stage.icons += Image("iconr_256.png")
        super.start(stage)
        stage.width = 500.0
        //stage.height = 650.0
        stage.isResizable = false
    }
}

fun main() {
    launch<MyApp>()
}
