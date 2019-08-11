package pt.marmelo.ets2atsjobsync.client

import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.common.payload.JobPayload
import pt.marmelo.ets2atsjobsync.common.utils.JacksonUtils
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {

    val jobsList: Map<String, List<JobPayload>> = JacksonUtils.fromString(String(Files.readAllBytes(Paths.get("zdata/jobs.json"))))
    val save = Info(Game.ETS2).profiles[0].saves[0]
    //val save = Save(Game.ETS2, Paths.get("zdata"))
    val jobs = save.extractJobs()
    //save.replaceJobs(jobsList)
    //val jobs = save.extractJobs()

    val json = JacksonUtils.toString(jobs)
    Files.write(Paths.get("zdata/jobs.json"), json.toByteArray())
    //val readValue: Map<String, List<Job>> = JacksonUtils.fromString(json)

    /*val read = SiiFile(Paths.get("zdata/1.35/game.sii")).read()
    Files.write(Paths.get("zdata/1.35/game_dec.sii"), read)*/
}
