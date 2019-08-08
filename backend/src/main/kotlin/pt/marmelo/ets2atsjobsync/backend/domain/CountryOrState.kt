package pt.marmelo.ets2atsjobsync.backend.domain

import pt.marmelo.ets2atsjobsync.common.Game
import javax.persistence.*

@Entity
data class CountryOrState(
        val name: String,
        @OneToMany(
                mappedBy = "countryOrState",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY,
                orphanRemoval = true
        )
        val cities: MutableSet<City>,
        @Enumerated(EnumType.STRING)
        val game: Game
) : DomainObject() {
}