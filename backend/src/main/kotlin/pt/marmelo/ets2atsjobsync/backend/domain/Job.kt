package pt.marmelo.ets2atsjobsync.backend.domain

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Job(
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "target_id")
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
        val fillRatio: Int
        //val trailerPlace: MutableList<String>
) : DomainObject() {

    val isHighPowerCargo: Boolean
        get() = trailerVariant.contains("overweight") || trailerDefinition.contains("overweight")

    val isHeavyCargo: Boolean
        get() = companyTruck.startsWith("heavy")

}