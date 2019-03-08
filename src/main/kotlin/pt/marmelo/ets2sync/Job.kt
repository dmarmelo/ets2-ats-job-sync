package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.util.readPropery

class Job(
    var target: String,
    var expirationTime: Long,
    var urgency: Int,
    var shortestDistanceKm: Int,
    var ferryTime: Int,
    var ferryPrice: Int,
    var cargo: String,
    var companyTruck: String,
    var trailerVariant: String,
    var trailerDefinition: String,
    var unitsCount: Int,
    var fillRatio: Int,
    var trailerPlace: Int
) {
    class Builder {
        lateinit var target: String
        var expirationTime: Long = 0
        var urgency: Int = 0
        var shortestDistanceKm: Int = 0
        var ferryTime: Int = 0
        var ferryPrice: Int = 0
        lateinit var cargo: String
        lateinit var companyTruck: String
        lateinit var trailerVariant: String
        lateinit var trailerDefinition: String
        var unitsCount: Int = 0
        var fillRatio: Int = 0
        var trailerPlace: Int = 0

        fun target(target: String) = apply { this.target = target }
        fun expirationTime(expirationTime: Long) = apply { this.expirationTime = expirationTime }
        fun urgency(urgency: Int) = apply { this.urgency = urgency }
        fun shortestDistanceKm(shortestDistanceKm: Int) = apply { this.shortestDistanceKm = shortestDistanceKm }
        fun ferryTime(ferryTime: Int) = apply { this.ferryTime = ferryTime }
        fun ferryPrice(ferryPrice: Int) = apply { this.ferryPrice = ferryPrice }
        fun cargo(cargo: String) = apply { this.cargo = cargo }
        fun companyTruck(companyTruck: String) = apply { this.companyTruck = companyTruck }
        fun trailerVariant(trailerVariant: String) = apply { this.trailerVariant = trailerVariant }
        fun trailerDefinition(trailerDefinition: String) = apply { this.trailerDefinition = trailerDefinition }
        fun unitsCount(unitsCount: Int) = apply { this.unitsCount = unitsCount }
        fun fillRatio(fillRatio: Int) = apply { this.fillRatio = fillRatio }
        fun trailerPlace(trailerPlace: Int) = apply { this.trailerPlace = trailerPlace }

        fun build() = Job(target, expirationTime, urgency, shortestDistanceKm, ferryTime, ferryPrice, cargo, companyTruck, trailerVariant, trailerDefinition, unitsCount, fillRatio, trailerPlace)
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
        TRAILER_VARIANT("trailer_variant"),
        TRAILER_DEFINITION("trailer_definition"),
        UNITS_COUNT("units_count"),
        FILL_RATIO("fill_ratio",1),
        TRAILER_PLACE("trailer_place",0);

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
        private var _hasDefault: Boolean = false
        val hasDefault: Boolean
            get() = _hasDefault
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
            this._hasDefault = true
        }

        constructor(propertyName: String, isQuoted: Boolean = false, quotedIfContains: String? = null) {
            this.propertyName = propertyName
            this.isQuoted = isQuoted
            this.quotedIfContains = quotedIfContains
        }

        fun formatValue(job: Job): String {
            val value = job.readPropery<Any>(jobPropertyName)
            return this.formatValue(value)!!
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
            if (hasDefault) {
                return when (blankValue) {
                    is String -> if (isQuotedOnBlank) "\"$blankValue\"" else blankValue
                    is Int -> if (isQuotedOnBlank) "\"$blankValue\"" else blankValue.toString()
                    is Long -> if (isQuotedOnBlank) "\"$blankValue\"" else blankValue.toString()
                    else -> null
                }
            }
            return null
        }
    }
}
