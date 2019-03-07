package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.parser.Context
import pt.marmelo.ets2sync.parser.ParseCallback
import java.nio.file.Path
import java.util.HashMap
import kotlin.collections.ArrayList

class Save(
    game: Game,
    directory: Path
) : AbstractSave(directory, SII_BASENAME, NAME_ATTRIBUTE, SAVE_TIME_ATTRIBUTE) {
    private companion object {
        const val SII_BASENAME = "info.sii"
        const val SAVE_BASENAME = "game.sii"
        const val NAME_ATTRIBUTE = "name"
        const val SAVE_TIME_ATTRIBUTE = "file_time"
        const val INFO_GAME_TIME_ATTRIBUTE = "time"
        const val DEPEND_ATTRIBUTE = "dependencies["
        const val ECONOMY_UNIT = "economy"
        const val GAME_TIME_ATTRIBUTE = "game_time"
        const val COMPANY_UNIT = "company"
        const val JOB_UNIT = "job_offer_data"
        const val COMPANY_NAME_PREFIX = "company.volatile."
    }

    private var _gameTime: Long = 0
    val gameTime: Long
        get() = _gameTime

    private var _dlcs: MutableList<String> = ArrayList()
    val dlcs: List<String>
        get() = _dlcs.toList()

    init {
        _game = game
        init()
    }


    override fun processAtribute(context: Context, name: String, value: String) {
        if (context == Context.ATTRIBUTE) {
            if (name.startsWith(DEPEND_ATTRIBUTE)) {
                val split = value.split("|")
                val dlcName = split[1].split("_")[1]
                _dlcs.add(dlcName)
            }
            else if (name == INFO_GAME_TIME_ATTRIBUTE) {
                _gameTime = value.toLong()
            }
        }
    }

    fun extractJobs(): Map<String, List<Job>> {
        val jobs: MutableMap<String, MutableList<Job>> = HashMap()
        var inJob = false
        var skipJob = false
        var job: Job.Builder = Job.Builder()
        var companyName = ""

        val save = directory.resolve(SAVE_BASENAME)
        SiiFile(save).parse(ParseCallback { context, name, value, _, _ ->
            if (context == Context.UNIT_START) {
                if (name == COMPANY_UNIT) {
                    companyName = value.replace(COMPANY_NAME_PREFIX, "")
                    jobs[companyName] = ArrayList()
                }
                else if (name == JOB_UNIT) {
                    inJob = true
                    job = Job.Builder()
                }
            }
            else if (context == Context.UNIT_END) {
                if (inJob) {
                    if (!skipJob)
                        jobs[companyName]!!.add(job.build())
                    inJob = false
                    skipJob = false
                }
            }
            else if (context == Context.ATTRIBUTE) {
                if (inJob && !skipJob) {
                    when(name) {
                        Job.Properties.TARGET.propertyName -> {
                            if (value.isEmpty())
                                skipJob = true
                            else
                                job.target = value
                        }
                        Job.Properties.EXPIRATION_TIME.propertyName -> job.expirationTime = value.toLong()
                        Job.Properties.URGENCY.propertyName -> job.urgency = value.toInt()
                        Job.Properties.DISTANCE.propertyName -> job.distance = value.toInt()
                        Job.Properties.FERRY_TIME.propertyName -> job.ferryTime = value.toInt()
                        Job.Properties.FERRY_PRICE.propertyName -> job.ferryPrice = value.toInt()
                        Job.Properties.CARGO.propertyName -> job.cargo = value
                        Job.Properties.COMPANY_TRUCK.propertyName -> job.companyTruck = value
                        Job.Properties.TRAILER_VARIANT.propertyName -> job.trailerVariant = value
                        Job.Properties.TRAILER_DEFINITION.propertyName -> job.trailerDefinition = value
                        Job.Properties.UNITS_COUNT.propertyName -> job.unitsCount = value.toInt()
                        Job.Properties.FILL_RATIO.propertyName -> job.fillRatio = value.toInt()
                        Job.Properties.TRAILER_PLACE.propertyName -> job.trailerPlace = value.toInt()
                    }
                }
            }
        })
        return jobs
    }

    fun replaceJobList(jobs: Map<String, List<Job>>): Boolean {
        val save = directory.resolve(SAVE_BASENAME)
        SiiFile(save).parse(ParseCallback { context, name, value, _, _ ->

        })
        return true
    }

    override fun validate(): Boolean {
        return true
    }
}
