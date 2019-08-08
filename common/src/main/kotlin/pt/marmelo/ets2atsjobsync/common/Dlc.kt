package pt.marmelo.ets2atsjobsync.common

enum class Dlc(
        val game: Game?,
        val id: String,
        val title: String
) {
    // COMMON
    BASE_GAME(null, "base", "Base Game"),

    // ETS2
    GOING_EAST(Game.ETS2, "dlc|eut2_east", "Going East!"),
    SCANDINAVIA(Game.ETS2, "dlc|eut2_north", "Scandinavia"),
    FRANCE(Game.ETS2, "dlc|eut2_fr", "Vive la France!"),
    ITALIA(Game.ETS2, "dlc|eut2_it", "Italia"),
    BALTIC(Game.ETS2, "dlc|eut2_balt", "Beyond the Baltic Sea"),

    // ATS
    ARIZONA(Game.ATS, "", ""),
    MEXICO(Game.ATS, "", ""),
    OREGON(Game.ATS, "", ""),
    WASHINGTON(Game.ATS, "", ""),
    UTAH(Game.ATS, "", ""),

    // CARGO
    HIGH_POWER_CARGO(Game.ETS2, "rdlc|eut2_trailers", "High Power Cargo Pack"), // overweight in trailer
    HEAVY_CARGO(Game.ETS2, "rdlc|eut2_heavy_cargo", "Heavy Cargo Pack") // heavy in company_truck
}