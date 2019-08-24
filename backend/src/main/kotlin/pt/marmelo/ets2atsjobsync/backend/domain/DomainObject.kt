package pt.marmelo.ets2atsjobsync.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.Instant
import javax.persistence.*


@MappedSuperclass
@JsonIgnoreProperties(value = ["createdAt", "updatedAt"], allowGetters = true)
abstract class DomainObject : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: Instant? = null

    @Version
    //@UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: Instant? = null
}