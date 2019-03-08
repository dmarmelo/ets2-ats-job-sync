package pt.marmelo.ets2sync.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions

fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

fun ByteArray.toHexString() : String {
    return this.joinToString("") { String.format("%02x", it) }
}

fun <R: Any?> Any.readPropery(propertyName: String): R {
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
