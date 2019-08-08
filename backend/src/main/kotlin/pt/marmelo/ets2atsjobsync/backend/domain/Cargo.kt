package pt.marmelo.ets2atsjobsync.backend.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
data class Cargo(
        val name: String,
        @OneToMany(
                mappedBy = "cargo",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        val jobs: MutableSet<Job>
) : DomainObject() {
}