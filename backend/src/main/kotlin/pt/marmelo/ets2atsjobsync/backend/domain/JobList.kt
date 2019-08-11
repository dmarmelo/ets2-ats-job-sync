package pt.marmelo.ets2atsjobsync.backend.domain

import javax.persistence.*

@Entity
data class JobList(
    val hash: String,
    @ManyToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "job_list_job",
        joinColumns = [JoinColumn(name = "job_list_id")],
        inverseJoinColumns = [JoinColumn(name = "job_id")]
    )
    val jobs: Set<Job> = HashSet()
) : DomainObject() {
}