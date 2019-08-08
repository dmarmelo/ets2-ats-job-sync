package pt.marmelo.ets2atsjobsync.backend.domain.converter

import javax.persistence.AttributeConverter

class CommaDelimitedStrings : AttributeConverter<List<String>, String> {
    companion object {
        const val DELIMITER = ","
    }
    override fun convertToDatabaseColumn(list: List<String>?): String {
        list ?: return ""
        return list.joinToString(separator = DELIMITER)
    }

    override fun convertToEntityAttribute(string: String?): List<String> {
        return if (string.isNullOrEmpty()) emptyList()
        else string!!.split(DELIMITER)
    }
}