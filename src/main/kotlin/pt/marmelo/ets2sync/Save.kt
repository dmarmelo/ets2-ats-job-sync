package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.parser.Context
import pt.marmelo.ets2sync.parser.ParseCallback
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
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
        var currentCompany = ""

        val save = directory.resolve(SAVE_BASENAME)
        SiiFile(save).parse(ParseCallback { context, name, value, _, _ ->
            if (context == Context.UNIT_START) {
                if (name == COMPANY_UNIT) {
                    currentCompany = value.replace(COMPANY_NAME_PREFIX, "")
                    jobs[currentCompany] = ArrayList()
                }
                else if (name == JOB_UNIT) {
                    inJob = true
                    job = Job.Builder()
                }
            }
            else if (context == Context.UNIT_END) {
                if (inJob) {
                    if (!skipJob)
                        jobs[currentCompany]!!.add(job.build())
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

    fun replaceJobs(jobs: Map<String, List<Job>>): Boolean {
        var currentCompany: String
        val newSaveData = StringBuilder("SiiNunit\r\n{")
        var inEconomy = false
        var inJob = false
        var hasGameTime = false
        var newLineHasValue = false
        var jobsAdded = 0
        var companyJobIndex = 0

        var companyJobs: List<Job> = Collections.emptyList()
        var currentJob = Job("", 0, 0, 0, 0, 0, "", "", "", "", 0, 0, 0)
        var useEmptyJob = false

        val save = directory.resolve(SAVE_BASENAME)
        SiiFile(save).parse(ParseCallback { context, name, value, sourceValue, _ ->
            if (context == Context.UNIT_START) {
                newSaveData.append("\r\n").append(name).append(" : ").append(value).append(" {\r\n")
                if (name == COMPANY_UNIT) {
                    currentCompany = value.replace(COMPANY_NAME_PREFIX, "")
                    if (jobs.containsKey(currentCompany)) {
                        companyJobs = jobs.getValue(currentCompany)
                    }
                    else {
                        companyJobs = Collections.emptyList()
                    }
                    companyJobIndex = 0
                }
                else if (name == JOB_UNIT) {
                    inJob = true
                    if(companyJobIndex < companyJobs.size) {
                        currentJob = companyJobs[companyJobIndex]
                    }
                    else {
                        useEmptyJob = true
                    }
                }
            }
            else if (context == Context.UNIT_END) {
                if (inJob) {
                    companyJobIndex++
                    inJob = false
                    useEmptyJob = false
                }
                newSaveData.append("}\r\n")
            }
            else if (context == Context.ATTRIBUTE) {
                newSaveData.append(" ").append(name).append(": ")
                newLineHasValue = false
                if (inJob) {
                    when(name) {
                        Job.Properties.TARGET.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.TARGET.formatValue(currentJob.target))
                                newLineHasValue = true
                                jobsAdded++
                            }
                            else if (Job.Properties.TARGET.hasDefault) {
                                newSaveData.append(Job.Properties.TARGET.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.EXPIRATION_TIME.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(gameTime + 30000)
                                newLineHasValue = true
                            }
                            else if (Job.Properties.EXPIRATION_TIME.hasDefault) {
                                newSaveData.append(Job.Properties.EXPIRATION_TIME.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.URGENCY.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.URGENCY.formatValue(currentJob.urgency))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.URGENCY.hasDefault) {
                                newSaveData.append(Job.Properties.URGENCY.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.DISTANCE.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.DISTANCE.formatValue(currentJob.distance))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.DISTANCE.hasDefault) {
                                newSaveData.append(Job.Properties.DISTANCE.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.FERRY_TIME.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.FERRY_TIME.formatValue(currentJob.ferryTime))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.FERRY_TIME.hasDefault) {
                                newSaveData.append(Job.Properties.FERRY_TIME.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.FERRY_PRICE.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.FERRY_PRICE.formatValue(currentJob.ferryPrice))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.FERRY_PRICE.hasDefault) {
                                newSaveData.append(Job.Properties.FERRY_PRICE.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.CARGO.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.CARGO.formatValue(currentJob.cargo))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.CARGO.hasDefault) {
                                newSaveData.append(Job.Properties.CARGO.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.COMPANY_TRUCK.propertyName -> {
                            if (!useEmptyJob) {
                                val needsQuotes = currentJob.companyTruck.contains("/")
                                newSaveData.append(Job.Properties.COMPANY_TRUCK.formatValue(if (needsQuotes) "\"${currentJob.companyTruck}\"" else currentJob.companyTruck))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.COMPANY_TRUCK.hasDefault) {
                                newSaveData.append(Job.Properties.COMPANY_TRUCK.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.TRAILER_VARIANT.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.TRAILER_VARIANT.formatValue(currentJob.trailerVariant))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.TRAILER_VARIANT.hasDefault) {
                                newSaveData.append(Job.Properties.TRAILER_VARIANT.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.TRAILER_DEFINITION.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.TRAILER_DEFINITION.formatValue(currentJob.trailerDefinition))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.TRAILER_DEFINITION.hasDefault) {
                                newSaveData.append(Job.Properties.TRAILER_DEFINITION.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.UNITS_COUNT.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.UNITS_COUNT.formatValue(currentJob.unitsCount))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.UNITS_COUNT.hasDefault) {
                                newSaveData.append(Job.Properties.UNITS_COUNT.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.FILL_RATIO.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.FILL_RATIO.formatValue(currentJob.fillRatio))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.FILL_RATIO.hasDefault) {
                                newSaveData.append(Job.Properties.FILL_RATIO.defaultValue())
                                newLineHasValue = true
                            }
                        }
                        Job.Properties.TRAILER_PLACE.propertyName -> {
                            if (!useEmptyJob) {
                                newSaveData.append(Job.Properties.TRAILER_PLACE.formatValue(currentJob.trailerPlace))
                                newLineHasValue = true
                            }
                            else if (Job.Properties.TRAILER_PLACE.hasDefault) {
                                newSaveData.append(Job.Properties.TRAILER_PLACE.defaultValue())
                                newLineHasValue = true
                            }
                        }
                    }
                }
                if (!newLineHasValue) {
                    newSaveData.append(sourceValue)
                }
                newSaveData.append("\r\n")
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
