package pt.marmelo.ets2sync.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.IOException

class JacksonUtils {
    companion object {
        val OBJECT_MAPPER: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

        fun <T> fromString(string: String, clazz: Class<T>): T {
            try {
                return OBJECT_MAPPER.readValue(string, clazz)
            } catch (e: IOException) {
                throw IllegalArgumentException("The given string value: $string cannot be transformed to Json object")
            }

        }

        inline fun <reified T> fromString(string: String): T {
            try {
                return OBJECT_MAPPER.readValue(string)
            } catch (e: IOException) {
                throw IllegalArgumentException("The given string value: $string cannot be transformed to Json object")
            }

        }

        fun <T> toString(value: T): String {
            try {
                return OBJECT_MAPPER.writeValueAsString(value)
            } catch (e: JsonProcessingException) {
                throw IllegalArgumentException("The given Json object value: $value cannot be transformed to a String")
            }

        }

        fun toJsonNode(value: String): JsonNode {
            try {
                return OBJECT_MAPPER.readTree(value)
            } catch (e: IOException) {
                throw IllegalArgumentException(e)
            }

        }

        inline fun <reified T> clone(value: T): T {
            return fromString(toString(value))
        }
    }
}