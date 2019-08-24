package pt.marmelo.ets2atsjobsync.backend.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
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
import pt.marmelo.ets2atsjobsync.common.utils.partition

@Service
class IngestService(
    private val ingestRepository: IngestRepository,
    private val jobRepository: JobRepository,
    private val cargoRepository: CargoRepository,
    private val companyRepository: CompanyRepository,
    private val asyncService: AsyncService,
    private val transactionTemplate: TransactionTemplate
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /*@Transactional
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
                    val cargo = cargoRepository.findByInternalId(job.cargo).orElseGet { cargoRepository.save(Cargo(job.cargo, "")) }
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
    }*/

    //@Transactional
    fun ingest(jobs: List<JobPayload>) {
        logger.info("Ingestion STARTED")
        val jobsListHash = DigestUtils.md5Hex(JacksonUtils.toString(jobs))
        // Check if the list was already ingested
        if (!ingestRepository.existsByHash(jobsListHash)) {
            val ingest: Ingest = ingestRepository.save(Ingest(jobsListHash))
            logger.info("Ingest Cargoes STARTED")
            jobs.map { it.cargo }.distinct().forEach {
                if (!cargoRepository.existsByInternalId(it))
                    cargoRepository.save(Cargo(it, ""))
            }
            logger.info("Ingest Cargoes ENDED")

            val companiesMap = companyRepository.findAll().map { it.internalId to it }.toMap()
            val cargoesMap = cargoRepository.findAll().map { it.internalId to it }.toMap()

            logger.info("Partitioning the list and initializing async threads")
            jobs.partition(200).forEach { jobsP ->
                asyncService.execute {
                    logger.info("Async Thread STARTED")
                    jobsP.forEach { job ->
                        val jobHash = DigestUtils.md5Hex(JacksonUtils.toString(job))
                        // Check if the job already exists
                        if (!jobRepository.existsByHash(jobHash)) {
//                            val source = companyRepository.findByInternalId(job.source).get()
//                            val target = companyRepository.findByInternalId(job.target).get()
//                            val cargo = cargoRepository.findByInternalId(job.cargo).get()
                            val source = companiesMap.getValue(job.source)
                            val target = companiesMap.getValue(job.target)
                            val cargo = cargoesMap.getValue(job.cargo)
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
                    logger.info("Async Thread ENDED")
                }
            }
        }
        logger.info("Ingestion ENDED")
    }
}