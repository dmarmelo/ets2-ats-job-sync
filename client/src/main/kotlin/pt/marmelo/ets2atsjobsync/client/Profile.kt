package pt.marmelo.ets2atsjobsync.client

import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.parser.Context
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class Profile(
    directory: Path
) : AbstractSave(directory, SII_BASENAME, NAME_ATTRIBUTE, SAVE_TIME_ATTRIBUTE) {
    private companion object {
        const val SII_BASENAME = "profile.sii"
        const val NAME_ATTRIBUTE = "profile_name"
        const val SAVE_TIME_ATTRIBUTE = "save_time"
        const val MAP_ATTRIBUTE = "map_path"
    }

    private var _saves: MutableList<Save> = ArrayList()
    val saves: List<Save>
        get() = _saves.toList()

    init {
        init()
        val savesDir = directory.resolve("save")
        Files.walk(savesDir, 1)
            .filter { f -> f != savesDir } // skip root
            .filter { f -> Files.isDirectory(f) }
            .filter { f -> !f.fileName.toString().contains("autosave") } // skip autosaves
            .forEach{ f ->
                val save = Save(game, f)
                if (save.isValid) {
                    _saves.add(save)
                }
            }
        _saves.sortDescending()
    }

    override fun processAtribute(context: Context, name: String, value: String) {
        if (context == Context.ATTRIBUTE) {
            if (name == MAP_ATTRIBUTE) {
                when(value) {
                    // TODO validate
                    Game.ETS2.mapValue -> game = Game.ETS2
                    Game.ATS.mapValue -> game = Game.ATS
                }
            }
        }
    }

    override fun validate(): Boolean {
        return game != Game.INVALID
    }

    override fun toString(): String {
        return "$name - $saveTime"
    }


}
