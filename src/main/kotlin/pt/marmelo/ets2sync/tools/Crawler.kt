package pt.marmelo.ets2sync.tools

import org.jsoup.Jsoup
import pt.marmelo.ets2sync.Game
import pt.marmelo.ets2sync.data.City
import pt.marmelo.ets2sync.dlc.BaseDlc
import pt.marmelo.ets2sync.dlc.Dlc
import pt.marmelo.ets2sync.dlc.Ets2Dlc
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
                    contains("east") -> Ets2Dlc.GOING_EAST
                    contains("scandinavia") -> Ets2Dlc.SCANDINAVIA
                    contains("france") -> Ets2Dlc.FRANCE
                    contains("italia") -> Ets2Dlc.ITALIA
                    contains("baltic") -> Ets2Dlc.BALTIC
                    else -> BaseDlc.BASE_GAME
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