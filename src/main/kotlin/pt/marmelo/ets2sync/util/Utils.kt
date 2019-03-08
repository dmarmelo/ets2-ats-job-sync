package pt.marmelo.ets2sync.util

import kotlin.reflect.full.declaredMemberProperties

fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

fun ByteArray.toHexString() : String {
    return this.joinToString("") { String.format("%02x", it) }
}

fun <R: Any?> Any.readPropery(propertyName: String): R {
    @Suppress("UNCHECKED_CAST")
    return this.javaClass.kotlin.declaredMemberProperties.first { it.name == propertyName }.get(this) as R
}