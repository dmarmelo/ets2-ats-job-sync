package pt.marmelo.ets2atsjobsync.common.payload

import pt.marmelo.ets2atsjobsync.common.Dlc
import pt.marmelo.ets2atsjobsync.common.utils.removeAccents

data class City(
    val name: String,
    val countryOrState: String,
    val numOfCompanies: Int,
    val dlc: Dlc
) {
    val internalId: String
        get() {
            return when(val internalId = convertSpecialCharacters(name.toLowerCase().removeAccents().replace(" ", ""))) {
                "banskabystrica" -> "bystrica"
                "clermont-ferrand" -> "clermont"
                "frankfurtammain" -> "frankfurt"
                "frederikshavn" -> "frederikshv"
                "klagenfurtamworthersee" -> "klagenfurt"
                "newcastle-upon-tyne" -> "newcastle"
                "praha" -> "prague"
                "saint-alban-du-rhone" -> "alban"
                "saint-laurent" -> "laurent"
                "saintpetersburg" -> "petersburg"
                "sosnovybor" -> "sosnovy_bor"
                "vasteras" -> "vasteraas"
                "villasangiovanni" -> "sangiovanni"
                else -> internalId
            }
        }

    private fun convertSpecialCharacters(input: String) : String {
        val sb = StringBuilder(input)
        for (i in 0 until sb.length) {
            when {
                sb[i] == '\u0141' -> { // Ł
                    sb.deleteCharAt(i)
                    sb.insert(i, 'L')
                }
                sb[i] == '\u0142' -> { // ł
                    sb.deleteCharAt(i)
                    sb.insert(i, 'l')
                }
                sb[i] == '\u00D8' -> { // Ø
                    sb.deleteCharAt(i)
                    sb.insert(i, 'O')
                }
                sb[i] == '\u00F8' -> { // ø
                    sb.deleteCharAt(i)
                    sb.insert(i, 'o')
                }
            }
        }
        return sb.toString()
    }

    override fun toString(): String {
        return "City(internalId='$internalId', name='$name', countryOrState='$countryOrState', numOfCompanies=$numOfCompanies, dlc=$dlc)"
    }
}
