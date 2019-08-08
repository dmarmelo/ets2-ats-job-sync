package pt.marmelo.ets2atsjobsync.common.utils

object ByteUtils {
    fun bytesToHex(hash: ByteArray): String {
        val hexString = StringBuilder()
        for (i in hash.indices) {
            val hex = Integer.toHexString(0xff and hash[i].toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}