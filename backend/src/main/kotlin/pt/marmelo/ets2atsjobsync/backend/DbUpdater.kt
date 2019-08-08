package pt.marmelo.ets2atsjobsync.backend

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import pt.marmelo.ets2atsjobsync.backend.domain.City
import pt.marmelo.ets2atsjobsync.backend.domain.CountryOrState
import pt.marmelo.ets2atsjobsync.backend.repository.CityRepository
import pt.marmelo.ets2atsjobsync.backend.repository.CountryOrStateRepository
import pt.marmelo.ets2atsjobsync.common.Dlc
import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.common.payloads.CityPayload
import javax.transaction.Transactional

@Component
class DbUpdater(
    val countryOrStateRepository: CountryOrStateRepository,
    val cityRepository: CityRepository
) : CommandLineRunner {

    @Value("classpath:ets2_cities.csv")
    lateinit var ets2Cities: Resource

    /*@Value("classpath:ats_cities.types")
    lateinit var atsCities: Resource*/

    @Transactional
    override fun run(vararg args: String?) {
        val cities = ets2Cities.inputStream.bufferedReader().useLines {
            it.drop(1).map { line ->
                val split = line.split(";")
                CityPayload(split[1], split[2], split[3].toInt(), Dlc.valueOf(split[4]))
            }.toList()
        }

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
    }
}