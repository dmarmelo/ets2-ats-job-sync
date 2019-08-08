package pt.marmelo.ets2atsjobsync.client

import pt.marmelo.ets2atsjobsync.client.data.Job
import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.parser.Context
import pt.marmelo.ets2atsjobsync.parser.ParseCallback
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.collections.ArrayList

class Save(
        game: Game,
        directory: Path
) : AbstractSave(directory, SII_BASENAME, NAME_ATTRIBUTE, SAVE_TIME_ATTRIBUTE) {
    companion object {
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

    var gameTime: Long = 0
        private set

    private var _dlcs: MutableList<String> = ArrayList()
    val dlcs: List<String>
        get() = _dlcs.toList()

    init {
        this.game = game
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
                gameTime = value.toLong()
            }
        }
    }

    fun extractJobs(): Map<String, List<Job>> {
        val jobs: MutableMap<String, MutableList<Job>> = HashMap()
        var inJob = false
        var skipJob = false
        var job = Job()
        var currentCompany = ""

        val save = directory.resolve(SAVE_BASENAME)
        SiiFile(save).parse(ParseCallback { context, name, value, _, _ ->
            if (context == Context.UNIT_START) {
                if (name == COMPANY_UNIT) {
                    currentCompany = value.replace(COMPANY_NAME_PREFIX, "")
                    jobs[currentCompany] = ArrayList()
                } else if (name == JOB_UNIT) {
                    inJob = true
                    job = Job()
                }
            } else if (context == Context.UNIT_END) {
                if (inJob) {
                    if (!skipJob)
                        jobs[currentCompany]!!.add(job)
                    inJob = false
                    skipJob = false
                }
            } else if (context == Context.ATTRIBUTE) {
                if (inJob && !skipJob) {
                    when (name) {
                        Job.Properties.TARGET.propertyName -> {
                            if (value.isEmpty())
                                skipJob = true
                            else
                                job.target = value
                        }
                        Job.Properties.EXPIRATION_TIME.propertyName -> job.expirationTime = value.toLong()
                        Job.Properties.URGENCY.propertyName -> job.urgency = value.toInt()
                        Job.Properties.DISTANCE.propertyName -> job.shortestDistanceKm = value.toInt()
                        Job.Properties.FERRY_TIME.propertyName -> job.ferryTime = value.toInt()
                        Job.Properties.FERRY_PRICE.propertyName -> job.ferryPrice = value.toInt()
                        Job.Properties.CARGO.propertyName -> job.cargo = value
                        Job.Properties.COMPANY_TRUCK.propertyName -> job.companyTruck = value
                        Job.Properties.TRAILER_VARIANT.propertyName -> job.trailerVariant = value
                        Job.Properties.TRAILER_DEFINITION.propertyName -> job.trailerDefinition = value
                        Job.Properties.UNITS_COUNT.propertyName -> job.unitsCount = value.toInt()
                        Job.Properties.FILL_RATIO.propertyName -> job.fillRatio = value.toInt()
                    }
                    if (name.startsWith(Job.Properties.TRAILER_PLACE.propertyName + "[")) {
                        job.addTrailerPlace(value)
                    }
                }
            }
        })
        return jobs
    }

    fun replaceJobs(jobs: Map<String, List<Job>>): Boolean {
        var currentCompany: String
        val newSaveData = StringBuilder("SiiNunit\r\n{")
        var inJob = false
        var newLineHasValue: Boolean
        var jobsAdded = 0
        var companyJobIndex = 0

        var companyJobs: List<Job> = Collections.emptyList()
        var currentJob = Job()
        var useEmptyJob = false

        val save = directory.resolve(SAVE_BASENAME)
        SiiFile(save).parse(ParseCallback { context, name, value, sourceValue, _ ->
            if (context == Context.UNIT_START) {
                newSaveData.append("\r\n").append(name).append(" : ").append(value).append(" {\r\n")
                if (name == COMPANY_UNIT) {
                    currentCompany = value.substring("company.volatile.".length)
                    if (jobs.containsKey(currentCompany)) {
                        companyJobs = jobs.getValue(currentCompany)
                    } else {
                        companyJobs = Collections.emptyList()
                    }
                    companyJobIndex = 0
                } else if (name == JOB_UNIT) {
                    inJob = true
                    if (companyJobIndex < companyJobs.size) {
                        currentJob = companyJobs[companyJobIndex]
                        // To allow job replacing on offline game
                        if (currentJob.cargo.contains("caravan"))
                            useEmptyJob = true
                    } else {
                        useEmptyJob = true
                    }
                }
            } else if (context == Context.UNIT_END) {
                if (inJob) {
                    companyJobIndex++
                    inJob = false
                    useEmptyJob = false
                }
                newSaveData.append("}\r\n")
            } else if (context == Context.ATTRIBUTE) {
                if (!inJob || Job.Properties.notList(name)) {
                    newSaveData.append(" ").append(name).append(": ")
                }
                newLineHasValue = false
                if (inJob) {
                    when (name) {
                        Job.Properties.EXPIRATION_TIME.propertyName -> {
                            newSaveData.append(if (!useEmptyJob) gameTime + 30000 else Job.Properties.EXPIRATION_TIME.defaultValue())
                            newLineHasValue = true
                            jobsAdded++
                        }
                        Job.Properties.TRAILER_PLACE.propertyName -> {
                            newSaveData.append(if (!useEmptyJob) Job.Properties.TRAILER_PLACE.formatValue(currentJob) else Job.Properties.TRAILER_PLACE.defaultValue())
                            newLineHasValue = true
                        }
                        else -> {
                            for (property in Job.Properties.values()) {
                                if (name == property.propertyName) {
                                    newSaveData.append(if (!useEmptyJob) property.formatValue(currentJob) else property.defaultValue())
                                    newLineHasValue = true
                                    break
                                }
                            }
                        }
                    }
                }
                if (Job.Properties.notList(name)) {
                    if (!newLineHasValue) {
                        newSaveData.append(sourceValue)
                    }
                    newSaveData.append("\r\n")
                }
            }
        })
        newSaveData.append("\r\n}\r\n")
        Files.write(save, newSaveData.toString().toByteArray())
        return true
    }

    override fun validate(): Boolean {
        return true
    }
}
