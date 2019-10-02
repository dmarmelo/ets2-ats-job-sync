package pt.marmelo.ets2atsjobsync.common

import java.nio.file.Path
import javax.swing.filechooser.FileSystemView

enum class Game(
    val saveDirName: String,
    val mapValue: String,
    val wikiCityListUrl: String
) {
    ETS2("Euro Truck Simulator 2", "/map/europe.mbd", "https://truck-simulator.fandom.com/wiki/List_of_Cities_in_Euro_Truck_Simulator_2"),
    ATS("American Truck Simulator", "/map/usa.mbd", "https://truck-simulator.fandom.com/wiki/List_of_Cities_in_American_Truck_Simulator"),
    INVALID("", "", "");

    // Returns the default ETS2 Settings directory for the current user
    // i.e. the current user's Documents\\Euro Truck Simulator 2
    val savePath: Path
        //get() = Paths.get(System.getProperty("user.home")).resolve("Documents").resolve(game.saveDirName)
        get() = FileSystemView.getFileSystemView().defaultDirectory.toPath().resolve(saveDirName)
}