package pt.marmelo.ets2atsjobsync.client

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

    protected var _game: Game = Game.INVALID
    val game: Game
        get() = _game

    private lateinit var _name: String
    val name: String
        get() = _name

    private lateinit var _saveTime: Instant
    val saveTime: Instant
        get() = _saveTime

    private var _isValid: Boolean = false
    val isValid: Boolean
        get() {
            if (!_isValid)
                _isValid = validate()
            return _isValid
        }

    // This is used to load and process the file passed as siiFileBasename to the constructor from the directory.
    // Should be called from the constructor of a derived class.
    fun init() {
        val save = directory.resolve(siiFileBaseName)
        SiiFile(save).parse(ParseCallback { context, name, value, _, _ ->
            if (context == Context.ATTRIBUTE) {
                when (name) {
                    nameAttribute -> _name = value
                    saveTimeAttribute -> _saveTime = Instant.ofEpochSecond(value.toLong())
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