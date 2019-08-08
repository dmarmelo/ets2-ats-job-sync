package pt.marmelo.ets2atsjobsync.client.tools

import org.jsoup.Jsoup
import pt.marmelo.ets2atsjobsync.common.Dlc
import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.common.payload.City
import java.io.File
import java.nio.file.Files

fun cities(game: Game) : List<City> {
    if (game == Game.INVALID)
        return emptyList()

    val html = Jsoup.connect(game.wikiCityListUrl).get()
    val table = html.select("table.article-table")[0]
    val rows = table.select("tr")

    val cities = mutableListOf<City>()
    for (row in rows) {
        var td = row.select("td")
        if (td.size == 0) { // If true means it is the header row
            td = row.select("th")
        }
        else {
            /* Index 0 -> City Name
             * Index 1 -> Country or State
             * Index 2 -> No. of Depots
             * Index 4 -> Expansion
             */
            //println("${td[0].text()},${td[1].text()},${td[2].text()},${td[4].text()}")
            val dlc: Dlc = with(td[4].text().toLowerCase()) {
                when {
                    contains("east") -> Dlc.GOING_EAST
                    contains("scandinavia") -> Dlc.SCANDINAVIA
                    contains("france") -> Dlc.FRANCE
                    contains("italia") -> Dlc.ITALIA
                    contains("baltic") -> Dlc.BALTIC
                    else -> Dlc.BASE_GAME
                }
            }
            cities.add(City(td[0].text(), td[1].text(), td[2].text().toInt(), dlc))
        }
    }
    return cities.sortedBy { it.internalId }
}

fun exportCitiesToCsv(game: Game, outFile: File) {
    Files.write(outFile.toPath(),
        ("InternalId;Name;CountryOrState;NumOfCompanies;Dlc\n" +
                cities(game).joinToString(separator = "\n", postfix = "\n") { "${it.internalId};${it.name};${it.countryOrState};${it.numOfCompanies};${it.dlc}" }).toByteArray())
}

fun main(args: Array<String>) {
    val citiesList = cities(Game.ETS2)
    citiesList.forEach(::println)
    println("Number of Cities: ${citiesList.size}")

    exportCitiesToCsv(Game.ETS2, File("zdata/ets2_cities.csv"))

}