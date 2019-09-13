package pt.marmelo.ets2atsjobsync.backend.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
data class Company(
    @NaturalId
    val internalId: String, // <name>.<city>
    val name: String,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonManagedReference
    val city: City,
    val cargoSlots: Int,
    @OneToMany(
        mappedBy = "source",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @JsonBackReference
    val sourceJobs: MutableSet<Job> = HashSet(),
    @OneToMany(
        mappedBy = "target",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @JsonBackReference
    val targetJobs: MutableSet<Job> = HashSet()
) : DomainObject() {
}