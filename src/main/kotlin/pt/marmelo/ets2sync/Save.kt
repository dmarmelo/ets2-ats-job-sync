package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.parser.Context
import java.nio.file.Path
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
                val name = split[1].split("_")[1]
                _dlcs.add(name)
            }
            else if (name == INFO_GAME_TIME_ATTRIBUTE) {
                _gameTime = value.toLong()
            }
        }
    }

    fun replaceJobList(jobs: List<Job>): Boolean {

        return true
    }

    override fun validate(): Boolean {
        return true
    }
}
