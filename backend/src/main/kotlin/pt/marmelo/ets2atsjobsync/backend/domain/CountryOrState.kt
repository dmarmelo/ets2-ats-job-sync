package pt.marmelo.ets2atsjobsync.backend.domain

import com.fasterxml.jackson.annotation.JsonBackReference
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
    @JsonBackReference
    val cities: MutableSet<City> = HashSet()
) : DomainObject() {
}