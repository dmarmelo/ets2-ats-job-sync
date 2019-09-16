package pt.marmelo.ets2atsjobsync.backend.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.marmelo.ets2atsjobsync.backend.domain.Job
import pt.marmelo.ets2atsjobsync.backend.repository.JobRepository
import pt.marmelo.ets2atsjobsync.common.Dlc
import pt.marmelo.ets2atsjobsync.common.Game

@RestController
class MainController(
    private val jobRepository: JobRepository
) {
    @GetMapping("dlcs/{game}")
    fun getDlcs(@PathVariable game: Game): ResponseEntity<List<Dlc>> {
        return ResponseEntity(Dlc.from(game), HttpStatus.OK)
    }

    @GetMapping("jobs")
    fun jobs(@RequestParam page: Int,
             @RequestParam size: Int,
             @RequestParam(defaultValue = "DESC") sortDirection: List<Sort.Direction>,
             @RequestParam(defaultValue = "createdAt") sortBy: List<String>
             ): Page<Job> {
        val sortOrders = sortDirection.zip(sortBy).map {
            Sort.Order(it.first, it.second)
        }.toList()
        return jobRepository.findAll(PageRequest.of(page, size, Sort.by(sortOrders)))
    }

    @GetMapping("jobs/{id}")
    fun jobs(@PathVariable id: Long) = jobRepository.findById(id)
}