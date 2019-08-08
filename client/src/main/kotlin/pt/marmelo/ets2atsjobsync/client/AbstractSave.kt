package pt.marmelo.ets2atsjobsync.client

import pt.marmelo.ets2atsjobsync.common.Game
import pt.marmelo.ets2atsjobsync.parser.Context
import pt.marmelo.ets2atsjobsync.parser.ParseCallback
import java.nio.file.Path
import java.time.Instant

abstract class AbstractSave(
    val directory: Path,
    val siiFileBaseName: String,
    val nameAttribute: String,
    val saveTimeAttribute: String
) : Comparable<AbstractSave> {

    var game: Game = Game.INVALID
        protected set

    lateinit var name: String
        private set

    lateinit var saveTime: Instant
        private set

    var isValid: Boolean = false
        get() {
            if (!field)
                field = validate()
            return field
        }

    // This is used to load and process the file passed as siiFileBasename to the constructor from the directory.
    // Should be called from the constructor of a derived class.
    fun init() {
        val save = directory.resolve(siiFileBaseName)
        SiiFile(save).parse(ParseCallback { context, name, value, _, _ ->
            if (context == Context.ATTRIBUTE) {
                when (name) {
                    nameAttribute -> this.name = value
                    saveTimeAttribute -> saveTime = Instant.ofEpochSecond(value.toLong())
                }
            }
            processAtribute(context, name, value)
        })

        // TODO
        //_isValid = validate()
    }

    // This function is called during init() for every attribute/value pair read from the file.
    // It should set mName (else the object will be named "[no name]") and
    // it must set mSaveTime (else the object will be considered invalid)
    protected abstract fun processAtribute(context: Context, name: String, value: String)

    // This function is called after all attributes have been sent to processAttribute.
    // Its return value determines whether the object is valid (true) or not (false).
    // Note that an object that fails parsing or has no save time is always invalid.
    protected abstract fun validate() : Boolean

    override fun compareTo(other: AbstractSave): Int {
        return saveTime.compareTo(other.saveTime)
    }
}