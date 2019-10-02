package pt.marmelo.ets2atsjobsync.client

import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.parser.CfgParser
import pt.marmelo.ets2atsjobsync.parser.Context
import java.nio.file.Files
import java.util.*

class Info(
    val game: Game
) {
    companion object {
        const val CONFIG_FILE_NAME = "config.cfg"
        const val PROFILES_DIR = "profiles"
        const val SAVE_FORMAT_VAR_NAME = "g_save_format"
    }

    private var _profiles: MutableList<Profile> = ArrayList()
    val profiles: List<Profile>
        get() = _profiles.toList()

    init {
        val profilesDir = game.savePath.resolve(PROFILES_DIR)
        Files.walk(profilesDir, 1)
            .filter { f -> f != profilesDir } // skip root
            .filter { f -> Files.isDirectory(f) }
            .forEach{ f ->
                val profile = Profile(f)
                if (profile.isValid) {
                    _profiles.add(profile)
                }
            }
        _profiles.sortDescending()
    }

    fun getSaveFormat(): SaveFormat {
        val fileContent = Files.readAllBytes(game.savePath.resolve(CONFIG_FILE_NAME))
        var saveFormat = SaveFormat.NOT_FOUND
        CfgParser.parse(fileContent) { context, name, value, _, _ ->
            if (context != Context.EMPTY_LINE && context == Context.CFG_PROPERTY) {
                if (name == SAVE_FORMAT_VAR_NAME) {
                    saveFormat = when(value.toInt()) {
                        SaveFormat.BINARY.value, 1 -> SaveFormat.BINARY
                        SaveFormat.TEXT.value -> SaveFormat.TEXT
                        SaveFormat.BOTH.value -> SaveFormat.BOTH
                        else -> SaveFormat.INVALID
                    }
                }

            }
        }
        return saveFormat
    }

    fun isValid(): Boolean {
        return true
    }


}