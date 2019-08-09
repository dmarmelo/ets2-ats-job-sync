package pt.marmelo.ets2atsjobsync.common.payload

import com.fasterxml.jackson.annotation.JsonIgnore

data class CompanyPayload(
    val name: String,
    val cityInternalId: String,
    val cargoSlots: Int
) {
    @get:JsonIgnore
    val internalId: String
        get() = "$name.$cityInternalId"

    override fun toString(): String {
        return "Company(internalId='$internalId', name='$name', city='$cityInternalId', cargoSlots=$cargoSlots)"
    }
}