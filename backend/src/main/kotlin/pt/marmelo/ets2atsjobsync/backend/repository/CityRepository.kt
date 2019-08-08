package pt.marmelo.ets2atsjobsync.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.marmelo.ets2atsjobsync.backend.domain.City
import java.util.*

interface CityRepository : JpaRepository<City, Long> {
    fun findByInternalId(name: String): Optional<City>
    fun existsByInternalId(name: String): Boolean
}