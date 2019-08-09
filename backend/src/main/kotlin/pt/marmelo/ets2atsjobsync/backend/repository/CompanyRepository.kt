package pt.marmelo.ets2atsjobsync.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.marmelo.ets2atsjobsync.backend.domain.Company
import java.util.*

interface CompanyRepository : JpaRepository<Company, Long> {
    fun findByInternalId(internalId: String): Optional<Company>
    fun existsByInternalId(internalId: String): Boolean
}