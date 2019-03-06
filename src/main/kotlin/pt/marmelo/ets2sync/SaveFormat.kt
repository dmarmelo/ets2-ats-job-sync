package pt.marmelo.ets2sync

enum class SaveFormat(val value: Int) {
    BINARY(0),
    TEXT(2),
    BOTH(3),
    INVALID(-1),
    NOT_FOUND(-2)

}