package pt.marmelo.ets2atsjobsync.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.marmelo.ets2atsjobsync.backend.domain.Company

interface CompanyRepository : JpaRepository<Company, Long> {
}