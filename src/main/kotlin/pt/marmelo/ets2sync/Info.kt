package pt.marmelo.ets2sync

import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.filechooser.FileSystemView

class Info(
    val game: Game
) {
    companion object {
        const val CONFIG_FILE_NAME = "config.cfg"
        const val PROFILES_DIR = "profiles"
        const val SAVE_FORMAT_VAR_NAME = "g_save_format"

        // Returns the default ETS2 Settings directory for the current user
        // i.e. the current user's Documents\\Euro Truck Simulator 2
        fun getDefaultDirectory(game: Game): Path {
            //return Paths.get(System.getProperty("user.home")).resolve("Documents").resolve(game.saveDirName)
            return FileSystemView.getFileSystemView().defaultDirectory.toPath().resolve(game.saveDirName)
        }
    }

    fun isValid(): Boolean {
        return true
    }


}