package pt.marmelo.ets2sync.dlc

enum class BaseDlc(
    override val id: String,
    override val title: String
) : Dlc {
    BASE_GAME("base", "Base Game")
}