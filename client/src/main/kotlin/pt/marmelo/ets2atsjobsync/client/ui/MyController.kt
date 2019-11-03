package pt.marmelo.ets2atsjobsync.client.ui

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.paint.Color
import pt.marmelo.ets2atsjobsync.client.Info
import pt.marmelo.ets2atsjobsync.client.Profile
import pt.marmelo.ets2atsjobsync.client.Save
import pt.marmelo.ets2atsjobsync.client.SaveFormat
import pt.marmelo.ets2atsjobsync.common.Dlc
import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.common.payload.JobPayload
import pt.marmelo.ets2atsjobsync.common.utils.JacksonUtils
import tornadofx.Controller
import tornadofx.observable
import tornadofx.onChange
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class MyController : Controller() {
    var gameUpdating = false
    val selectedGame = SimpleStringProperty(Game.ETS2.name)
    val gameDir = SimpleStringProperty(Game.ETS2.savePath.toString())
    val saveFormat = SimpleStringProperty()
    val saveFormatStatus = Label().apply { style = "-fx-font-weight: bold" }
    private lateinit var gameInfo: Info
    var profileList = mutableListOf<Profile>().observable()
    val selectedProfile = SimpleObjectProperty<Profile>()
    var saveList = mutableListOf<Save>().observable()
    val selectedSave = SimpleObjectProperty<Save>()

    val dlcCheckBoxes = mutableListOf<CheckBox>()

    val availableJobLists = mutableListOf<String>().observable()
    val selectedJobList = SimpleStringProperty()

    val status = SimpleStringProperty("Ready!")

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
        val saveFormatEnum = gameInfo.getSaveFormat()
        saveFormat.value = saveFormatEnum.name
        when (saveFormatEnum) {
            SaveFormat.TEXT -> {
                saveFormatStatus.text = "Settings OK"
                saveFormatStatus.textFill = Color.GREEN
            }
            SaveFormat.BINARY, SaveFormat.BOTH -> {
                saveFormatStatus.text = "The Save Format needs to be \"Text\"! (g_save_format 2)"
                saveFormatStatus.textFill = Color.RED
            }
            SaveFormat.NOT_FOUND -> {
                saveFormatStatus.text = "The save format was not found in the settings file."
                saveFormatStatus.textFill = Color.RED
            }
            else -> {
                saveFormatStatus.text = "Unknown save format."
                saveFormatStatus.textFill = Color.RED
            }
        }
        profileList.clear()
        profileList.addAll(gameInfo.profiles)
        selectedProfile.value = profileList[0]
        saveList.clear()
        saveList.addAll(gameInfo.profiles[0].saves)
        selectedSave.value = saveList[0]

        // TODO disable checkbox for the dlc's not in the save
        /*checkBoxesList.forEach { cb ->
            if (saveList[0].dlcs.any { cb.userData as Dlc }) {

            }

        }*/

        gameUpdating = false
    }

    fun selectAllDlcs() {
        val toClear = dlcCheckBoxes.any { it.isSelected }
        dlcCheckBoxes.forEach {
            it.isSelected = !toClear
        }
    }

    private fun createJobDirs() {
        listOf(
            Game.ETS2.name,
            Game.ATS.name,
            "ProMods"
        )
            .forEach { File(it).mkdir() }
    }

    private fun updateJobLists(game: String) {
        availableJobLists.clear()
        val currentDirectory = Paths.get(game)
        Files.walk(currentDirectory, 1)
            .filter { f -> f != currentDirectory } // skip root
            .filter { f -> Files.isRegularFile(f) }
            .forEach { f -> availableJobLists.add(f.toFile().name) }
        availableJobLists.sort()
        if (!availableJobLists.isEmpty())
            selectedJobList.value = availableJobLists[0]
    }

    fun sync() {
        val jobListPath = Paths.get(selectedGame.value).resolve(selectedJobList.value)
        var jobsList: List<JobPayload> = JacksonUtils.fromString(String(Files.readAllBytes(jobListPath)))
        dlcCheckBoxes.forEach {
            if (!it.isDisabled && !it.isSelected)
                jobsList = Dlc.removeCargoDlc(it.userData as Dlc, jobsList)
        }
        selectedSave.value.replaceJobs(jobsList)
    }

    fun extract(file: File) {
        val extractedJobs = selectedSave.value.extractJobs()
        val json = JacksonUtils.toString(extractedJobs)
        Files.write(file.toPath(), json.toByteArray())
    }

    fun clearJobs() {
        selectedSave.value.replaceJobs(emptyList())
    }

    private fun gameFromString(game: String): Game =
        when (game) {
            Game.ETS2.name, "ProMods" -> Game.ETS2
            Game.ATS.name -> Game.ATS
            else -> Game.INVALID
        }

    fun refresh() {
        status.value = "Refreshing..."
        updateData(gameFromString(selectedGame.value))
        updateJobLists(selectedGame.value)
        status.value = "Refreshed!"
    }
}