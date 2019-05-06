package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.util.JacksonUtils
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {

    val jobsList: Map<String, List<Job>> = JacksonUtils.fromString(String(Files.readAllBytes(Paths.get("zdata/jobs.json"))))
    val save = Info(Game.ETS2).profiles[0].saves[0]
    //val save = Save(Game.ETS2, Paths.get("zdata"))
    //val jobs = save.extractJobs()
    save.replaceJobs(jobsList)
    //val jobs = save.extractJobs()

    /*val json = JacksonUtils.toString(companies)
    Files.write(Paths.get("zdata/jobs.json"), json.toByteArray())*/
    //val readValue: Map<String, List<Job>> = JacksonUtils.fromString(json)

    /*val read = SiiFile(Paths.get("test/game_old.sii")).read()
    Files.write(Paths.get("test/game_dec.sii"), read)*/
}
