package pt.marmelo.ets2atsjobsync.backend

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import pt.marmelo.ets2atsjobsync.backend.domain.City
import pt.marmelo.ets2atsjobsync.backend.domain.Company
import pt.marmelo.ets2atsjobsync.backend.domain.CountryOrState
import pt.marmelo.ets2atsjobsync.backend.repository.CityRepository
import pt.marmelo.ets2atsjobsync.backend.repository.CompanyRepository
import pt.marmelo.ets2atsjobsync.backend.repository.CountryOrStateRepository
import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.common.payload.CityPayload
import pt.marmelo.ets2atsjobsync.common.payload.CompanyPayload
import pt.marmelo.ets2atsjobsync.common.utils.JacksonUtils
import javax.transaction.Transactional

@Component
class DbUpdater(
    private val countryOrStateRepository: CountryOrStateRepository,
    private val cityRepository: CityRepository,
    private val companyRepository: CompanyRepository
) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(DbUpdater::class.java)

    @Value("classpath:ets2_cities.json")
    lateinit var ets2Cities: Resource
    @Value("classpath:ets2_companies.json")
    lateinit var ets2Companies: Resource

    /*@Value("classpath:ats_cities.types")
    lateinit var atsCities: Resource*/

    @Transactional
    override fun run(vararg args: String?) {
        // Obtain Ets2 cities list
        logger.info("Started Database Update")
        val cities: List<CityPayload> = JacksonUtils.fromString(ets2Cities.inputStream.bufferedReader().use { it.readText() })
        /*val cities = ets2Cities.inputStream.bufferedReader().useLines {
            it.drop(1).map { line ->
                val split = line.split(";")
                CityPayload(split[1], split[2], split[3].toInt(), Dlc.valueOf(split[4]))
            }.toList()
        }*/
        // Update the database
        cities.groupBy { it.countryOrState }.forEach {
            val countryName = it.key
            val countryCities = it.value

            val country =
                if (!countryOrStateRepository.existsByName(countryName))
                    countryOrStateRepository.save(CountryOrState(countryName, Game.ETS2))
                else
                    countryOrStateRepository.findByName(countryName).get()

            countryCities.forEach { city ->
                if (!cityRepository.existsByInternalId(city.internalId))
                    cityRepository.save(City(city.internalId, city.name, country, city.dlc))
            }
        }

        // Obtain Ets2 companies list
        val companies: List<CompanyPayload> = JacksonUtils.fromString(ets2Companies.inputStream.bufferedReader().use { it.readText() })
        /*val companies = ets2Companies.inputStream.bufferedReader().useLines {
            it.drop(1).map { line ->
                val split = line.split(";")
                CompanyPayload(split[1], split[2], split[3].toInt())
            }.toList()
        }*/
        // Update the database
        companies.groupBy { it.cityInternalId }.forEach {
            val cityInternalId = it.key

            if (cityRepository.existsByInternalId(cityInternalId)) {
                val city = cityRepository.findByInternalId(cityInternalId).get()
                val cityCompanies = it.value

                cityCompanies.forEach { company ->
                    if (!companyRepository.existsByInternalId(company.internalId))
                        companyRepository.save(Company(company.internalId, company.name, city, company.cargoSlots))
                }
            }
            else {
                logger.warn("City '%s' not found while inserting companies!", cityInternalId)
            }
        }

        logger.info("Database Update Complete")
    }
}