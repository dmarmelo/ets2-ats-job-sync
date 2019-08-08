package pt.marmelo.ets2atsjobsync.backend.domain

import org.hibernate.annotations.NaturalId
import pt.marmelo.ets2atsjobsync.backend.domain.converter.CommaDelimitedStrings
import pt.marmelo.ets2atsjobsync.common.payloads.JobPayload
import javax.persistence.*

@Entity
data class Job(
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    val target: Company,
    val urgency: Int,
    val shortestDistanceKm: Int,
    val ferryTime: Int,
    val ferryPrice: Int,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id")
    val cargo: Cargo,
    val companyTruck: String,
    val trailerVariant: String,
    val trailerDefinition: String,
    val unitsCount: Int,
    val fillRatio: Int,
    @Convert(converter = CommaDelimitedStrings::class)
    val trailerPlace: MutableList<String>,
    @NaturalId
    val hash: String // calculate the hash of the json to compare if it already exists
) : DomainObject() {

    val isHighPowerCargo: Boolean
        get() = trailerVariant.contains("overweight") || trailerDefinition.contains("overweight")

    val isHeavyCargo: Boolean
        get() = companyTruck.startsWith("heavy")

    fun toJobPayload(): JobPayload = JobPayload(
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