package pt.marmelo.ets2atsjobsync.backend.domain

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
data class Company(
    @NaturalId
    val internalId: String, // <name>.<city>
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
    val jobs: MutableSet<Job> = HashSet()
) : DomainObject() {
}