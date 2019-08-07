package pt.marmelo.ets2sync.data

import com.fasterxml.jackson.annotation.JsonIgnore
import pt.marmelo.ets2sync.util.readPropery

data class Job(
    val target: String,
    @JsonIgnore
    val expirationTime: Long,
    val urgency: Int,
    val shortestDistanceKm: Int,
    val ferryTime: Int,
    val ferryPrice: Int,
    val cargo: String,
    val companyTruck: String,
    val trailerVariant: String,
    val trailerDefinition: String,
    val unitsCount: Int,
    val fillRatio: Int,
    val trailerPlace: List<String>
) {
    class Builder {
        var target: String = ""
        var expirationTime: Long = 0
        var urgency: Int = 0
        var shortestDistanceKm: Int = 0
        var ferryTime: Int = 0
        var ferryPrice: Int = 0
        var cargo: String = ""
        var companyTruck: String = ""
        var trailerVariant: String = ""
        var trailerDefinition: String = ""
        var unitsCount: Int = 0
        var fillRatio: Int = 0
        var trailerPlace: MutableList<String> = ArrayList()

        fun target(target: String) = apply { this.target = target }
        fun expirationTime(expirationTime: Long) = apply { this.expirationTime = expirationTime }
        fun expirationTime(expirationTime: String) = expirationTime(expirationTime.toLong())
        fun urgency(urgency: Int) = apply { this.urgency = urgency }
        fun urgency(urgency: String) = urgency(urgency.toInt())
        fun shortestDistanceKm(shortestDistanceKm: Int) = apply { this.shortestDistanceKm = shortestDistanceKm }
        fun shortestDistanceKm(shortestDistanceKm: String) = shortestDistanceKm(shortestDistanceKm.toInt())
        fun ferryTime(ferryTime: Int) = apply { this.ferryTime = ferryTime }
        fun ferryTime(ferryTime: String) = ferryTime(ferryTime.toInt())
        fun ferryPrice(ferryPrice: Int) = apply { this.ferryPrice = ferryPrice }
        fun ferryPrice(ferryPrice: String) = ferryPrice(ferryPrice.toInt())
        fun cargo(cargo: String) = apply { this.cargo = cargo }
        fun companyTruck(companyTruck: String) = apply { this.companyTruck = companyTruck }
        fun trailerVariant(trailerVariant: String) = apply { this.trailerVariant = trailerVariant }
        fun trailerDefinition(trailerDefinition: String) = apply { this.trailerDefinition = trailerDefinition }
        fun unitsCount(unitsCount: Int) = apply { this.unitsCount = unitsCount }
        fun unitsCount(unitsCount: String) = unitsCount(unitsCount.toInt())
        fun fillRatio(fillRatio: Int) = apply { this.fillRatio = fillRatio }
        fun fillRatio(fillRatio: String) = fillRatio(fillRatio.toInt())
        fun addTrailerPlace(trailerPlace: String) = apply { this.trailerPlace.add(trailerPlace) }

        fun build() = Job(
            target, expirationTime, urgency, shortestDistanceKm, ferryTime, ferryPrice, cargo, companyTruck,
            trailerVariant, trailerDefinition, unitsCount, fillRatio, trailerPlace
        )
    }

    enum class Properties {
        TARGET("target", "", true, true),
        EXPIRATION_TIME("expiration_time","nil"),
        URGENCY("urgency","nil"),
        DISTANCE("shortest_distance_km",0),
        FERRY_TIME("ferry_time",0),
        FERRY_PRICE("ferry_price",0),
        CARGO("cargo","null"),
        COMPANY_TRUCK("company_truck","", false, true, "/"),
        TRAILER_VARIANT("trailer_variant", "null"),
        TRAILER_DEFINITION("trailer_definition", "null"),
        UNITS_COUNT("units_count", 0),
        FILL_RATIO("fill_ratio",1),
        TRAILER_PLACE("trailer_place", true);

        val propertyName: String
        val jobPropertyName: String
            get() {
                val split = propertyName.split("_")
                return if (split.size == 1) {
                    propertyName
                } else {
                    var camelCase = split[0]
                    for (i in 1..(split.size - 1)) {
                        camelCase += split[i][0].toUpperCase() + split[i].substring(1)
                    }
                    camelCase
                }
            }
        private lateinit var blankValue: Any
        private var isQuoted: Boolean = false
        private var isQuotedOnBlank: Boolean = false
        private var quotedIfContains: String? = null

        constructor(propertyName: String, blankValue: Any, isQuoted: Boolean = false, isQuotedOnBlank: Boolean = false, quotedIfContains: String? = null) {
            this.propertyName = propertyName
            this.blankValue = blankValue
            this.isQuoted = isQuoted
            this.isQuotedOnBlank = isQuotedOnBlank
            this.quotedIfContains = quotedIfContains
        }

        private var _isList: Boolean = false
        val isList: Boolean
            get() = _isList

        constructor(propertyName: String, isList: Boolean) {
            this.propertyName = propertyName
            this._isList = isList
        }

        fun formatValue(job: Job): String {
            return if (isList) {
                val value = job.readPropery<List<*>>(jobPropertyName)
                val formattedList = StringBuilder()
                formattedList.append(value.size)
                for ((i, p) in value.withIndex()) {
                    formattedList.append("\r\n $propertyName[$i]: $p")
                }
                formattedList.toString()
            } else {
                val value = job.readPropery<Any>(jobPropertyName)
                formatValue(value)!!
            }
        }

        fun formatValue(value: Any): String? {
            return when (value) {
                is String -> {
                    val needsQuotes = if (quotedIfContains != null) value.contains(quotedIfContains!!) else false
                    if (isQuoted || needsQuotes) "\"$value\"" else value
                }
                is Int -> if (isQuoted) "\"$value\"" else value.toString()
                is Long -> if (isQuoted) "\"$value\"" else value.toString()
                else -> null
            }
        }

        fun defaultValue(): Any? {
            return if (isList) 0
            else {
                when (blankValue) {
                    is String -> if (isQuotedOnBlank) "\"$blankValue\"" else blankValue
                    is Int -> if (isQuotedOnBlank) "\"$blankValue\"" else blankValue.toString()
                    is Long -> if (isQuotedOnBlank) "\"$blankValue\"" else blankValue.toString()
                    else -> null
                }
            }
        }

        companion object {
            fun isList(name: String): Boolean {
                for (p in values()) {
                    if(p.isList && name.startsWith(p.propertyName + "["))
                        return true
                }
                return false
            }

            fun notList(name: String) = !isList(name)
        }
    }
}
