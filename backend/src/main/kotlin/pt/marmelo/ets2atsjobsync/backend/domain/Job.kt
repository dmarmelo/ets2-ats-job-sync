package pt.marmelo.ets2atsjobsync.backend.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.NaturalId
import pt.marmelo.ets2atsjobsync.backend.domain.converter.PipeDelimitedStrings
import pt.marmelo.ets2atsjobsync.common.payload.JobPayload
import javax.persistence.*

@Entity
data class Job(
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    @JsonManagedReference
    val source: Company,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    @JsonManagedReference
    val target: Company,
    val urgency: Int,
    val shortestDistanceKm: Int,
    val ferryTime: Int,
    val ferryPrice: Int,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cargo_id", nullable = false)
    @JsonManagedReference
    val cargo: Cargo,
    val companyTruck: String,
    val trailerVariant: String,
    val trailerDefinition: String,
    val unitsCount: Int,
    val fillRatio: Int,
    @Convert(converter = PipeDelimitedStrings::class)
    val trailerPlace: MutableList<String>,
    @NaturalId
    val hash: String, // calculate the hash of the json to compare if it already exists
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ingest_id", nullable = false)
    @JsonManagedReference
    val ingest: Ingest
) : DomainObject() {

    val isHighPowerCargo: Boolean
        get() = toJobPayload().isHighPowerCargo

    val isHeavyCargo: Boolean
        get() = toJobPayload().isHeavyCargo

    fun toJobPayload(): JobPayload = JobPayload(
        source.internalId,
        target.internalId,
        urgency,
        shortestDistanceKm,
        ferryTime,
        ferryPrice,
        cargo.internalId,
        companyTruck,
        trailerVariant,
        trailerDefinition,
        unitsCount,
        fillRatio,
        trailerPlace
    )
}