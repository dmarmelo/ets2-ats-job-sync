package pt.marmelo.ets2atsjobsync.backend.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pt.marmelo.ets2atsjobsync.backend.domain.Job
import pt.marmelo.ets2atsjobsync.common.Game

interface JobRepository : JpaRepository<Job, Long> {
    fun existsByHash(hash: String): Boolean
    @Query("select j from Job j join j.source s join s.city ci join ci.countryOrState co where co.game = :game")
    fun findByGame(game: Game, pageable: Pageable): List<Job>
}