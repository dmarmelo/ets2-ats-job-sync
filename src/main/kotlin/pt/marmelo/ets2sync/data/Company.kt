package pt.marmelo.ets2sync.data

data class Company(
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