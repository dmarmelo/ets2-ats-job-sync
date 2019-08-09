package pt.marmelo.ets2atsjobsync.client.tools

import pt.marmelo.ets2atsjobsync.client.Save
import pt.marmelo.ets2atsjobsync.client.SiiFile
import pt.marmelo.ets2atsjobsync.common.payload.CompanyPayload
import pt.marmelo.ets2atsjobsync.common.utils.JacksonUtils
import pt.marmelo.ets2atsjobsync.parser.Context
import pt.marmelo.ets2atsjobsync.parser.ParseCallback
import java.io.File
import java.nio.file.Files

fun extractCompanies(save: File) : List<CompanyPayload> {
    val companies = mutableListOf<CompanyPayload>()
    var inCompany = false
    var currentCompany = ""
    SiiFile(save.toPath()).parse(ParseCallback { context, name, value, _, _ ->
        if (context == Context.UNIT_START) {
            if (name == Save.COMPANY_UNIT) {
                inCompany = true
                currentCompany = value.substring("company.volatile.".length)
            }
        } else if (context == Context.UNIT_END) {
            if (inCompany) {
                inCompany = false
            }
        } else if (context == Context.ATTRIBUTE) {
            if (inCompany) {
                if (name == "job_offer") {
                    companies.add(CompanyPayload(currentCompany.substringBefore('.'), currentCompany.substringAfter('.'), value.toInt()))
                }
            }
        }
    })
    return companies.sortedBy { it.internalId }
}

fun extractCompaniesToCsv(save: File, outFile: File) {
    Files.write(outFile.toPath(),
        ("InternalId;Name;CityInternalId;CargoSlots\n" +
                extractCompanies(save).joinToString(separator = "\n", postfix = "\n") { "${it.internalId};${it.name};${it.cityInternalId};${it.cargoSlots}" }).toByteArray())
}

fun extractCompaniesToJson(save: File, outFile: File) {
    Files.write(outFile.toPath(), JacksonUtils.toString(extractCompanies(save)).toByteArray())
}

fun extractCities(save: File) : List<String> = extractCompanies(save).map { it.cityInternalId }.distinct()

fun extractCompanyNames(save: File) : List<String> = extractCompanies(save).map { it.name }.distinct()

fun extractVisitedCities(save: File) : Set<String> {
    val cities = mutableSetOf<String>()
    var inEconomy = false
    SiiFile(save.toPath()).parse(ParseCallback { context, name, value, _, _ ->
        if (context == Context.UNIT_START) {
            if (name == "economy") {
                inEconomy = true
            }
        } else if (context == Context.UNIT_END) {
            if (inEconomy) {
                inEconomy = false
            }
        } else if (context == Context.ATTRIBUTE) {
            if (inEconomy) {
                if (name.startsWith("visited_cities[")) {
                    cities.add(value)
                }
            }
        }
    })
    return cities
}

fun main() {
    /*val extractCompanies = extractCompanies(File("zdata/1.35/game.sii"))
    extractCompanies.forEach(::println)
    println("${extractCompanies.size} companies")

    val extractCities = extractCities(File("zdata/1.35/game.sii"))
    extractCities.forEach(::println)
    println("${extractCities.size} cities")

    val extractCompanyNames = extractCompanyNames(File("zdata/1.35/game.sii"))
    extractCompanyNames.forEach(::println)
    println("${extractCompanyNames.size} companies")*/

    //extractCompaniesToCsv(File("zdata/1.35/game.sii"), File("zdata/ets2_companies.csv"))
    extractCompaniesToJson(File("zdata/1.35/game.sii"), File("zdata/ets2_companies.json"))
}
