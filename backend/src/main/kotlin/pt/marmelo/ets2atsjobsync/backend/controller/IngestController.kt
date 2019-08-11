package pt.marmelo.ets2atsjobsync.backend.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.marmelo.ets2atsjobsync.backend.service.IngestService
import pt.marmelo.ets2atsjobsync.common.payload.JobPayload

@RestController
class IngestController(
    private val ingestService: IngestService
) {
    @PostMapping("/ingest")
    fun ingest(@RequestBody jobs: List<JobPayload>): ResponseEntity<Unit> {
        ingestService.ingest(jobs)
        return ResponseEntity(HttpStatus.OK)
    }
}