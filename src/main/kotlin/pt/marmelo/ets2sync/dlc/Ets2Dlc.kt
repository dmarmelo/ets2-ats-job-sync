package pt.marmelo.ets2sync.dlc

enum class Ets2Dlc(
    override val id: String,
    override val title: String
) : Dlc {
    GOING_EAST("dlc|eut2_east", "Going East!"),
    SCANDINAVIA("dlc|eut2_north", "Scandinavia"),
    FRANCE("dlc|eut2_fr", "Vive la France!"),
    ITALIA("dlc|eut2_it", "Italia"),
    BALTIC("dlc|eut2_balt", "Beyond the Baltic Sea"),
    HIGH_POWER_CARGO("rdlc|eut2_trailers", "High Power Cargo Pack"), // overweight in trailer
    HEAVY_CARGO("rdlc|eut2_heavy_cargo", "Heavy Cargo Pack"); // heavy in company_truck
}