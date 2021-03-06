package pt.marmelo.ets2atsjobsync.backend.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import org.hibernate.annotations.NaturalId
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
data class Cargo(
    @NaturalId
    val internalId: String,
    val name: String,
    @OneToMany(
        mappedBy = "cargo",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @JsonBackReference
    val jobs: MutableSet<Job> = HashSet()
) : DomainObject() {
}