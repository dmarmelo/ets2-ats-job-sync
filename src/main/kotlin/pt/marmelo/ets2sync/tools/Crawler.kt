package pt.marmelo.ets2sync.tools

import org.jsoup.Jsoup
import java.io.File
import java.nio.file.Files

fun cities(url: String, outFile: File) {
    val html = Jsoup.connect(url).get()
    val table = html.select("table.article-table")[0]
    val rows = table.select("tr")

    val sb = StringBuilder()
    for (row in rows) {
        var td = row.select("td")
        if (td.size == 0)
            td = row.select("th")
        println("${td[0].text()},${td[1].text()},${td[2].text()},${td[4].text()}")
        sb.append("${td[0].text()},${td[1].text()},${td[2].text()},${td[4].text()}\n")
    }
    Files.write(outFile.toPath(), sb.toString().toByteArray())
}

fun ets2Cities(outFile: File) {
    cities(
        "https://truck-simulator.fandom.com/wiki/List_of_Cities_in_Euro_Truck_Simulator_2",
        outFile
    )
}

fun atsCities(outFile: File) {
    cities(
        "https://truck-simulator.fandom.com/wiki/List_of_Cities_in_American_Truck_Simulator",
        outFile
    )
}


fun main(args: Array<String>) {
    ets2Cities(File("zdata/ets2_cities.csv"))
    //atsCities(File("zdata/ats_cities.csv"))
}