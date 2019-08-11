package pt.marmelo.ets2atsjobsync.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.marmelo.ets2atsjobsync.backend.domain.Cargo
import java.util.*

interface CargoRepository : JpaRepository<Cargo, Long> {
    fun findByInternalId(internalId: String): Optional<Cargo>
    fun existsByInternalId(internalId: String): Boolean
}