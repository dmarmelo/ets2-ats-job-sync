package pt.marmelo.ets2atsjobsync.common.utils

import pt.marmelo.ets2atsjobsync.common.utils.ByteUtils.bytesToHex
import java.io.*
import java.nio.charset.StandardCharsets
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object DigestUtils {
    private const val STREAM_BUFFER_LENGTH = 8 * 1024

    enum class Algorithm(val jvmName: String) {
        MD2("MD2"),
        MD5("MD5"),
        SHA_1("SHA-1"),
        SHA_224("SHA-224"),
        SHA_256("SHA-256"),
        SHA_384("SHA-384"),
        SHA_512("SHA-512");
    }

    // Generic
    fun hash(data: ByteArray, algorithm: Algorithm): ByteArray {
        var digest: MessageDigest? = null
        try {
            digest = MessageDigest.getInstance(algorithm.jvmName)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return digest!!.digest(data)
    }

    fun hash(data: String, algorithm: Algorithm): ByteArray {
        return hash(data.toByteArray(StandardCharsets.UTF_8), algorithm)
    }

    fun hash(data: InputStream, algorithm: Algorithm): ByteArray {
        var digest: MessageDigest? = null
        try {
            digest = MessageDigest.getInstance(algorithm.jvmName)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        val buffer = ByteArray(STREAM_BUFFER_LENGTH)
        var count: Int = data.read(buffer)
        while (count > 0) {
            digest!!.update(buffer, 0, count)
            count = data.read(buffer)
        }
        return digest!!.digest()
    }

    fun hash(data: File, algorithm: Algorithm): ByteArray {
        BufferedInputStream(FileInputStream(data)).use { stream -> return hash(stream, algorithm) }
    }

    fun hashHex(data: ByteArray, algorithm: Algorithm): String {
        return bytesToHex(hash(data, algorithm))
    }

    fun hashHex(data: String, algorithm: Algorithm): String {
        return bytesToHex(hash(data, algorithm))
    }

    fun hashHex(data: InputStream, algorithm: Algorithm): String {
        return bytesToHex(hash(data, algorithm))
    }

    fun hashHex(data: File, algorithm: Algorithm): String {
        return bytesToHex(hash(data, algorithm))
    }

    // SHA-256
    fun sha256(data: ByteArray): ByteArray {
        return hash(data, Algorithm.SHA_256)
    }

    fun sha256(data: String): ByteArray {
        return hash(data, Algorithm.SHA_256)
    }

    fun sha256(data: InputStream): ByteArray {
        return hash(data, Algorithm.SHA_256)
    }

    fun sha256(data: File): ByteArray {
        return hash(data, Algorithm.SHA_256)
    }

    fun sha256Hex(data: ByteArray): String {
        return bytesToHex(sha256(data))
    }

    fun sha256Hex(data: String): String {
        return bytesToHex(sha256(data))
    }

    fun sha256Hex(data: InputStream): String {
        return bytesToHex(sha256(data))
    }

    fun sha256Hex(data: File): String {
        return bytesToHex(sha256(data))
    }

    // MD5
    fun md5(data: ByteArray): ByteArray {
        return hash(data, Algorithm.MD5)
    }

    fun md5(data: String): ByteArray {
        return hash(data, Algorithm.MD5)
    }

    fun md5(data: InputStream): ByteArray {
        return hash(data, Algorithm.MD5)
    }

    fun md5(data: File): ByteArray {
        return hash(data, Algorithm.MD5)
    }

    fun md5Hex(data: ByteArray): String {
        return bytesToHex(md5(data))
    }

    fun md5Hex(data: String): String {
        return bytesToHex(md5(data))
    }

    fun md5Hex(data: InputStream): String {
        return bytesToHex(md5(data))
    }

    fun md5Hex(data: File): String {
        return bytesToHex(md5(data))
    }

    // Copy and calculate hash
    fun copyGetHash(`in`: InputStream, out: OutputStream, algorithm: Algorithm): ByteArray {
        var digest: MessageDigest? = null
        try {
            digest = MessageDigest.getInstance(algorithm.jvmName)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        val dis = DigestInputStream(`in`, digest)
        val buffer = ByteArray(STREAM_BUFFER_LENGTH)
        var count: Int = dis.read(buffer)
        while (count > 0) {
            out.write(buffer, 0, count)
            count = dis.read(buffer)
        }
        digest = dis.messageDigest
        return digest!!.digest()
    }

    fun copyGetHash(`in`: InputStream, out: File, algorithm: Algorithm): ByteArray {
        out.parentFile.mkdirs()
        BufferedOutputStream(FileOutputStream(out)).use { outStream -> return copyGetHash(`in`, outStream, algorithm) }
    }

    fun copyGetHash(`in`: File, out: OutputStream, algorithm: Algorithm): ByteArray {
        BufferedInputStream(FileInputStream(`in`)).use { inStream -> return copyGetHash(inStream, out, algorithm) }
    }

    fun copyGetHash(`in`: File, out: File, algorithm: Algorithm): ByteArray {
        out.parentFile.mkdirs()
        BufferedInputStream(FileInputStream(`in`)).use { inStream -> BufferedOutputStream(FileOutputStream(out)).use { outStream -> return copyGetHash(inStream, outStream, algorithm) } }
    }

    fun copyGetHashHex(`in`: InputStream, out: OutputStream, algorithm: Algorithm): String {
        return bytesToHex(copyGetHash(`in`, out, algorithm))
    }

    fun copyGetHashHex(`in`: InputStream, out: File, algorithm: Algorithm): String {
        return bytesToHex(copyGetHash(`in`, out, algorithm))
    }

    fun copyGetHashHex(`in`: File, out: OutputStream, algorithm: Algorithm): String {
        return bytesToHex(copyGetHash(`in`, out, algorithm))
    }

    fun copyGetHashHex(`in`: File, out: File, algorithm: Algorithm): String {
        return bytesToHex(copyGetHash(`in`, out, algorithm))
    }

    // SHA-256
    fun copyGetSha256(`in`: InputStream, out: OutputStream): ByteArray {
        return copyGetHash(`in`, out, Algorithm.SHA_256)
    }

    fun copyGetSha256(`in`: InputStream, out: File): ByteArray {
        return copyGetHash(`in`, out, Algorithm.SHA_256)
    }

    fun copyGetSha256(`in`: File, out: OutputStream): ByteArray {
        return copyGetHash(`in`, out, Algorithm.SHA_256)
    }

    fun copyGetSha256(`in`: File, out: File): ByteArray {
        return copyGetHash(`in`, out, Algorithm.SHA_256)
    }

    fun copyGetSha256Hex(`in`: InputStream, out: OutputStream): String {
        return bytesToHex(copyGetSha256(`in`, out))
    }

    fun copyGetSha256Hex(`in`: InputStream, out: File): String {
        return bytesToHex(copyGetSha256(`in`, out))
    }

    fun copyGetSha256Hex(`in`: File, out: OutputStream): String {
        return bytesToHex(copyGetSha256(`in`, out))
    }

    fun copyGetSha256Hex(`in`: File, out: File): String {
        return bytesToHex(copyGetSha256(`in`, out))
    }

    // MD5
    fun copyGetMd5(`in`: InputStream, out: OutputStream): ByteArray {
        return copyGetHash(`in`, out, Algorithm.MD5)
    }

    fun copyGetMd5(`in`: InputStream, out: File): ByteArray {
        return copyGetHash(`in`, out, Algorithm.MD5)
    }

    fun copyGetMd5(`in`: File, out: OutputStream): ByteArray {
        return copyGetHash(`in`, out, Algorithm.MD5)
    }

    fun copyGetMd5(`in`: File, out: File): ByteArray {
        return copyGetHash(`in`, out, Algorithm.MD5)
    }

    fun copyGetMd5Hex(`in`: InputStream, out: OutputStream): String {
        return bytesToHex(copyGetMd5(`in`, out))
    }

    fun copyGetMd5Hex(`in`: InputStream, out: File): String {
        return bytesToHex(copyGetMd5(`in`, out))
    }

    fun copyGetMd5Hex(`in`: File, out: OutputStream): String {
        return bytesToHex(copyGetMd5(`in`, out))
    }

    fun copyGetMd5Hex(`in`: File, out: File): String {
        return bytesToHex(copyGetMd5(`in`, out))
    }

    fun isEqual(hash1: ByteArray, hash2: ByteArray): Boolean {
        return MessageDigest.isEqual(hash1, hash2)
    }

    fun isEqual(hash1: String, hash2: String): Boolean {
        return hash1 == hash2
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("MD2: ${hashHex("123", Algorithm.MD2)}");
        println("MD5: ${hashHex("123", Algorithm.MD5)}");
        println("SHA_1: ${hashHex("123", Algorithm.SHA_1)}");
        println("SHA_224: ${hashHex("123", Algorithm.SHA_224)}");
        println("SHA_256: ${hashHex("123", Algorithm.SHA_256)}");
        println("SHA_384: ${hashHex("123", Algorithm.SHA_384)}");
        println("SHA_512: ${hashHex("123", Algorithm.SHA_512)}");

        println("File: " + md5Hex(File("zdata/game_ets_text.sii")))
        println("Stream: " + copyGetMd5Hex(
                File("zdata/game_ets_text.sii"),
                File("zdata/game_ets_text-out.sii")))

        println("File: " + sha256Hex(File("zdata/game_ets_text.sii")))
        println("Stream: " + copyGetSha256Hex(
                File("zdata/game_ets_text.sii"),
                File("zdata/game_ets_text-out.sii")))
    }
}