package pt.marmelo.ets2atsjobsync.backend.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
data class Ingest(
    val hash: String,
    @OneToMany(
        mappedBy = "ingest",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    val jobs: Set<Job> = HashSet()
) : DomainObject() {
}