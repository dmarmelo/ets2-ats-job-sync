package pt.marmelo.ets2sync

enum class Game(
    val saveDirName: String,
    val mapValue: String,
    val wikiCityListUrl: String
) {
    ETS2("Euro Truck Simulator 2", "/map/europe.mbd", "https://truck-simulator.fandom.com/wiki/List_of_Cities_in_Euro_Truck_Simulator_2"),
    ATS("American Truck Simulator", "/map/usa.mbd", "https://truck-simulator.fandom.com/wiki/List_of_Cities_in_American_Truck_Simulator"),
    INVALID("", "", "")
}