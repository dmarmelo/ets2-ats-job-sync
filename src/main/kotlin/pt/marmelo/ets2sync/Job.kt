package pt.marmelo.ets2sync

class Job(
    var target: String,
    var expirationTime: Long,
    var urgency: Int,
    var distance: Int,
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
        var distance: Int = 0
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
        fun distance(distance: Int) = apply { this.distance = distance }
        fun ferryTime(ferryTime: Int) = apply { this.ferryTime = ferryTime }
        fun ferryPrice(ferryPrice: Int) = apply { this.ferryPrice = ferryPrice }
        fun cargo(cargo: String) = apply { this.cargo = cargo }
        fun companyTruck(companyTruck: String) = apply { this.companyTruck = companyTruck }
        fun trailerVariant(trailerVariant: String) = apply { this.trailerVariant = trailerVariant }
        fun trailerDefinition(trailerDefinition: String) = apply { this.trailerDefinition = trailerDefinition }
        fun unitsCount(unitsCount: Int) = apply { this.unitsCount = unitsCount }
        fun fillRatio(fillRatio: Int) = apply { this.fillRatio = fillRatio }
        fun trailerPlace(trailerPlace: Int) = apply { this.trailerPlace = trailerPlace }

        fun build() = Job(target, expirationTime, urgency, distance, ferryTime, ferryPrice, cargo, companyTruck, trailerVariant, trailerDefinition, unitsCount, fillRatio, trailerPlace)
    }

    enum class Properties {
        TARGET("target", String::class.java, "", true, true),
        EXPIRATION_TIME("expiration_time", Long::class.java,"nil"),
        URGENCY("urgency", String::class.java,"nil"),
        DISTANCE("shortest_distance_km", Int::class.java,0),
        FERRY_TIME("ferry_time", Int::class.java,0),
        FERRY_PRICE("ferry_price", Int::class.java,0),
        CARGO("cargo", String::class.java,"null"),
        COMPANY_TRUCK("company_truck", String::class.java,"", false, true),
        TRAILER_VARIANT("trailer_variant", String::class.java),
        TRAILER_DEFINITION("trailer_definition", String::class.java),
        UNITS_COUNT("units_count", Int::class.java),
        FILL_RATIO("fill_ratio", Int::class.java,1),
        TRAILER_PLACE("trailer_place", Int::class.java,0);

        private var type: Class<*>

        val propertyName: String
        private var _hasDefault: Boolean = false
        val hasDefault: Boolean
            get() = _hasDefault
        private lateinit var blankValue: Any
        private var isQuoted: Boolean = false
        private var isQuotedOnBlank: Boolean = false

        constructor(propertyName: String, type: Class<*>, blankValue: Any, isQuoted: Boolean = false, isQuotedOnBlank: Boolean = false) {
            this.propertyName = propertyName
            this.type = type
            this.blankValue = blankValue
            this.isQuoted = isQuoted
            this.isQuotedOnBlank = isQuotedOnBlank
            this._hasDefault = true

        }

        constructor(propertyName: String, type: Class<*>) {
            this.propertyName = propertyName
            this.type = type
        }

        fun formatValue(value: Any): String? {
            return when (value) {
                is String -> if (isQuoted) "\"$value\"" else value
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
