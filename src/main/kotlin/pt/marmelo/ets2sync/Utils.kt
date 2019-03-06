package pt.marmelo.ets2sync

fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

fun ByteArray.toHexString() : String {
    return this.joinToString("") { String.format("%02x", it) }
}