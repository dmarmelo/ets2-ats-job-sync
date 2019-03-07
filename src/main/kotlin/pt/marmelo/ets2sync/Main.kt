package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.util.JacksonUtils
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {

    val save = Save(Game.ETS2, Paths.get("Euro Truck Simulator 2/profiles/44616E69656C205B50545D/save/1"))
    val jobs = save.extractJobs()

    val json = JacksonUtils.toString(jobs)
    Files.write(Paths.get("test.json"), json.toByteArray())
    //val readValue: Map<String, List<Job>> = JacksonUtils.fromString(json)

}
