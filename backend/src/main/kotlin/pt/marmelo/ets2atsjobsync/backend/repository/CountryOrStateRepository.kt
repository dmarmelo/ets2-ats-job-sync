package pt.marmelo.ets2atsjobsync.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.marmelo.ets2atsjobsync.backend.domain.CountryOrState
import java.util.*

interface CountryOrStateRepository : JpaRepository<CountryOrState, Long> {
    fun findByName(name: String): Optional<CountryOrState>
    fun existsByName(name: String): Boolean
}