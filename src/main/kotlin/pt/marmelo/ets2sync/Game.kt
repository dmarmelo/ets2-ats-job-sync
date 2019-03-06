package pt.marmelo.ets2sync

enum class Game(
    val saveDirName: String,
    val mapValue: String
) {
    ETS2("Euro Truck Simulator 2", "/map/europe.mbd"),
    ATS("American Truck Simulator", "/map/usa.mbd")
}