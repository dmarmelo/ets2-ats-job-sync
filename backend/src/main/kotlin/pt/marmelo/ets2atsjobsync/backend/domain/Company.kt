package pt.marmelo.ets2atsjobsync.backend.domain

import javax.persistence.*

@Entity
data class Company(
        val name: String,
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "city_id")
        val city: City,
        val cargoSlots: Int,
        @OneToMany(
                mappedBy = "target",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        val jobs: MutableSet<Job>
) : DomainObject() {
}