package pt.marmelo.ets2atsjobsync.backend.domain

import org.hibernate.annotations.NaturalId
import pt.marmelo.ets2atsjobsync.common.Game
import javax.persistence.*

@Entity
data class CountryOrState(
    @NaturalId
    val name: String,
    @Enumerated(EnumType.STRING)
    val game: Game,
    @OneToMany(
        mappedBy = "countryOrState",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    val cities: MutableSet<City> = HashSet()
) : DomainObject() {
}