package pt.marmelo.ets2atsjobsync.common.payload

import pt.marmelo.ets2atsjobsync.common.utils.readProperty

data class JobPayload(
    var source: String = "",
    var target: String = "",
    /*@JsonIgnore
    var expirationTime: Long = 0,*/
    var urgency: Int = 0,
    var shortestDistanceKm: Int = 0,
    var ferryTime: Int = 0,
    var ferryPrice: Int = 0,
    var cargo: String = "",
    var companyTruck: String = "",
    var trailerVariant: String = "",
    var trailerDefinition: String = "",
    var unitsCount: Int = 0,
    var fillRatio: Int = 0,
    var trailerPlace: MutableList<String> = ArrayList()
) {
    fun addTrailerPlace(trailerPlace: String) = this.trailerPlace.add(trailerPlace)

    val isHighPowerCargo: Boolean
        get() = trailerVariant.contains("overweight") || trailerDefinition.contains("overweight")

    val isHeavyCargo: Boolean
        get() = companyTruck.startsWith("heavy")

    enum class Properties {
        TARGET("target", "", true, true),
        EXPIRATION_TIME("expiration_time", "nil"),
        URGENCY("urgency", "nil"),
        DISTANCE("shortest_distance_km", 0),
        FERRY_TIME("ferry_time", 0),
        FERRY_PRICE("ferry_price", 0),
        CARGO("cargo", "null"),
        COMPANY_TRUCK("company_truck", "", false, true, "/"),
        TRAILER_VARIANT("trailer_variant", "null"),
        TRAILER_DEFINITION("trailer_definition", "null"),
        UNITS_COUNT("units_count", 0),
        FILL_RATIO("fill_ratio", 1),
        TRAILER_PLACE("trailer_place", true);

        val propertyName: String
        val jobPropertyName: String
            get() {
                val split = propertyName.split("_")
                return if (split.size == 1)
                    propertyName
                else
                    split[0] + split.drop(1).map { it[0].toUpperCase() + it.substring(1) }.joinToString(separator = "")
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

        var isList: Boolean = false
            private set

        constructor(propertyName: String, isList: Boolean) {
            this.propertyName = propertyName
            this.isList = isList
        }

        fun formatValue(job: JobPayload): String {
            return if (isList) {
                val value = job.readProperty<List<*>>(jobPropertyName)
                val formattedList = StringBuilder()
                formattedList.append(value.size)
                value.forEachIndexed { i, p ->
                    formattedList.append("\r\n $propertyName[$i]: $p")
                }
                formattedList.toString()
            } else {
                val value = job.readProperty<Any>(jobPropertyName)
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
                values().forEach {
                    if (it.isList && name.startsWith(it.propertyName + "["))
                        return true
                }
                return false
            }

            fun notList(name: String) = !isList(name)

            fun findByPropertyName(propertyName: String): Properties? {
                return values().filter { it.propertyName == propertyName }.firstOrNull()
            }
        }
    }
}
