package pt.marmelo.ets2atsjobsync.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.marmelo.ets2atsjobsync.backend.domain.Ingest

interface IngestRepository : JpaRepository<Ingest, Long> {
    fun existsByHash(hash: String): Boolean
}