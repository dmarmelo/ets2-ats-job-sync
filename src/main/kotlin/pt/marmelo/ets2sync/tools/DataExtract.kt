package pt.marmelo.ets2sync.tools

import pt.marmelo.ets2sync.SiiFile
import pt.marmelo.ets2sync.parser.Context
import pt.marmelo.ets2sync.parser.ParseCallback
import java.io.File

fun extractCompanies(save: File): List<String> {
    val companies = mutableSetOf<String>()
    var inEconomy = false
    SiiFile(save.toPath()).parse(ParseCallback { context, name, value, _, _ ->
        if (context == Context.UNIT_START) {
            if (name == "economy") {
                inEconomy = true
            }
        }
        else if (context == Context.UNIT_END) {
            if (inEconomy) {
                inEconomy = false
            }
        }
        else if (context == Context.ATTRIBUTE) {
            if (inEconomy) {
                if (name.startsWith("companies[")) {
                    val companyName = value.substring("company.volatile.".length)
                    companies.add(companyName)
                }
            }
        }
    })
    return companies.toList()
}

fun extractCities(save: File): List<String> = extractCompanies(save).map { it.substringAfter('.') }.distinct()

fun extractCompanyNames(save: File): List<String> = extractCompanies(save).map { it.substringBefore('.') }.distinct()

fun extractVisitedCities(save: File): Set<String> {
    val cities = mutableSetOf<String>()
    var inEconomy = false
    SiiFile(save.toPath()).parse(ParseCallback { context, name, value, _, _ ->
        if (context == Context.UNIT_START) {
            if (name == "economy") {
                inEconomy = true
            }
        }
        else if (context == Context.UNIT_END) {
            if (inEconomy) {
                inEconomy = false
            }
        }
        else if (context == Context.ATTRIBUTE) {
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
    val extractCompanies = extractCompanies(File("zdata/1.35/game.sii"))
    val extractCities = extractVisitedCities(File("zdata/1.35/game.sii"))
    println(extractCompanies)
    println("${extractCompanies.size} companies")

    val out = extractCities(File("zdata/1.35/game.sii"))
    println(out.sorted())
    println("${out.size} cities")

    val out2 = extractCompanyNames(File("zdata/1.35/game.sii"))
    println(out2.sorted())
    println("${out2.size} companies")

    println(extractCities.sorted())
    println("${extractCities.size} cities")

    val list = out + extractCities
    val diff = list.groupBy { it }
        .filter { it.value.size == 1 }
        .flatMap { it.value }
    println(diff)

}