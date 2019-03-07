package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.parser.Context
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
        Collections.sort(_saves, Collections.reverseOrder())
        //_saves.sort()
    }

    override fun processAtribute(context: Context, name: String, value: String) {
        if (context == Context.ATTRIBUTE) {
            if (name == MAP_ATTRIBUTE) {
                when(value) {
                    // TODO validate
                    Game.ETS2.mapValue -> _game = Game.ETS2
                    Game.ATS.mapValue -> _game = Game.ATS
                }
            }
        }
    }

    override fun validate(): Boolean {
        return game != Game.INVALID
    }


}
