package pt.marmelo.ets2atsjobsync.backend.domain

import org.hibernate.annotations.NaturalId
import pt.marmelo.ets2atsjobsync.common.Dlc
import javax.persistence.*

@Entity
data class City(
        @NaturalId
        val internalId: String,
        val name: String,
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "country_or_state_id")
        val countryOrState: CountryOrState,
        @OneToMany(
                mappedBy = "city",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        val companies: MutableSet<Company>,
        @Enumerated(EnumType.STRING)
        val dlc: Dlc
) : DomainObject() {
}