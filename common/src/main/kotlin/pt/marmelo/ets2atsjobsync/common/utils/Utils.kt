package pt.marmelo.ets2atsjobsync.common.utils

import java.text.Normalizer
import kotlin.math.min
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions

fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

fun ByteArray.toHexString() : String {
    return this.joinToString("") { String.format("%02x", it) }
}

fun <R: Any?> Any.readProperty(propertyName: String): R {
    @Suppress("UNCHECKED_CAST")
    return this.javaClass.kotlin.declaredMemberProperties.first { it.name == propertyName }.get(this) as R
}

fun <R: Any?> Any.callMethod(methodName: String, vararg values: Any): R {
    @Suppress("UNCHECKED_CAST")
    return this.javaClass.kotlin.declaredMemberFunctions.first {
        it.name == methodName && it.parameters.size == values.size + 1
                && it.parameters.map { p -> p.type.toString() }.drop(1) == values.map { v -> v::class.qualifiedName }
    }.call(this, *values) as R
}

fun <T: Any?>String.callSelfParser(destClazz: KClass<*>): Any? {
    val methodName = when (destClazz) {
        Int::class -> "toInt"
        Char::class -> "toChar"
        Long::class -> "toLong"
        Byte::class -> "toByte"
        Float::class -> "toFloat"
        Short::class -> "toShort"
        Double::class -> "toDouble"
        Boolean::class -> "toBoolean"
        else -> ""
    }
    val parsingMethod: KFunction<*> = this::class.functions.first { it.name == methodName }
    return parsingMethod.call(this)
}

fun CharSequence.removeDiacriticals(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}

fun <T> List<T>.partition(groupSize: Int): List<List<T>> {
    val numberOfGroups = (this.size + groupSize - 1) / groupSize
    val result = mutableListOf<List<T>>()
    for (i in 0 until numberOfGroups) {
        result.add(this.subList(groupSize * i, min(groupSize * i + groupSize, this.size)))
    }
    return result
}
