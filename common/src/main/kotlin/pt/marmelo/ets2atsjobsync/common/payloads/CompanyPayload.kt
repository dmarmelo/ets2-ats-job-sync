package pt.marmelo.ets2atsjobsync.common.payloads

data class CompanyPayload(
    val name: String,
    val city: String,
    val cargoSlots: Int
) {
    val internalId: String
        get() = "$name.$city"

    override fun toString(): String {
        return "Company(internalId='$internalId', name='$name', city='$city', cargoSlots=$cargoSlots)"
    }
}