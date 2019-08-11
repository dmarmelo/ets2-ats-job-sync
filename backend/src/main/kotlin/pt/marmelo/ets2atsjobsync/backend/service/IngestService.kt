package pt.marmelo.ets2atsjobsync.backend.service

import org.springframework.stereotype.Service
import pt.marmelo.ets2atsjobsync.backend.domain.Cargo
import pt.marmelo.ets2atsjobsync.backend.domain.Ingest
import pt.marmelo.ets2atsjobsync.backend.domain.Job
import pt.marmelo.ets2atsjobsync.backend.repository.CargoRepository
import pt.marmelo.ets2atsjobsync.backend.repository.CompanyRepository
import pt.marmelo.ets2atsjobsync.backend.repository.IngestRepository
import pt.marmelo.ets2atsjobsync.backend.repository.JobRepository
import pt.marmelo.ets2atsjobsync.common.payload.JobPayload
import pt.marmelo.ets2atsjobsync.common.utils.DigestUtils
import pt.marmelo.ets2atsjobsync.common.utils.JacksonUtils
import javax.transaction.Transactional

@Service
class IngestService(
    private val ingestRepository: IngestRepository,
    private val jobRepository: JobRepository,
    private val cargoRepository: CargoRepository,
    private val companyRepository: CompanyRepository
) {
    @Transactional
    fun ingest(jobs: List<JobPayload>) {
        val jobsListHash = DigestUtils.md5Hex(JacksonUtils.toString(jobs))
        // Check if the list was already ingested
        if (!ingestRepository.existsByHash(jobsListHash)) {
            val ingest = ingestRepository.save(Ingest(jobsListHash))
            jobs.forEach { job ->
                val jobHash = DigestUtils.md5Hex(JacksonUtils.toString(job))
                // Check if the job already exists
                if (!jobRepository.existsByHash(jobHash)) {
                    // TODO Should already exist, but should be created here if it does not?
                    val source = companyRepository.findByInternalId(job.source).get()
                    val target = companyRepository.findByInternalId(job.target).get()
                    val cargo =
                        if (!cargoRepository.existsByInternalId(job.cargo))
                            cargoRepository.save(Cargo(job.cargo, ""))
                        else
                            cargoRepository.findByInternalId(job.cargo).get()
                    jobRepository.save(
                        Job(source,
                            target,
                            job.urgency,
                            job.shortestDistanceKm,
                            job.ferryTime,
                            job.ferryPrice,
                            cargo,
                            job.companyTruck,
                            job.trailerVariant,
                            job.trailerDefinition,
                            job.unitsCount,
                            job.fillRatio,
                            job.trailerPlace,
                            jobHash,
                            ingest)
                    )
                }
            }
        }
    }
}