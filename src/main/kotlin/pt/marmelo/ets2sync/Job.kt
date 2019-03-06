package pt.marmelo.ets2sync

data class Job(
    val cargo: String,
    val variant: Int,
    val target: String,
    val urgency: Int,
    val distance: Int,
    val ferryTime: Int,
    val ferryPrice: Int,
    val companyTruck: String,
    val trailerPlace: Int
) {
    companion object {
        fun getBlank(): Job {
            return Job(
                "null",
                -1,
                "",
                -1,
                0,
                0,
                0,
                "",
                0)
        }
    }
}
