package pt.marmelo.ets2atsjobsync.backend.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
data class CountryOrState(
        val name: String,
        @OneToMany(
                mappedBy = "countryOrState",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        val cities: MutableSet<City>
) : DomainObject() {
}