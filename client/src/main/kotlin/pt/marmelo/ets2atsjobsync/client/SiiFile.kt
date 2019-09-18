package pt.marmelo.ets2atsjobsync.client

import pt.marmelo.ets2atsjobsync.common.utils.byteArrayOfInts
import pt.marmelo.ets2atsjobsync.parser.ParseCallback
import pt.marmelo.ets2atsjobsync.parser.SiiTextParser
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.Inflater
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class SiiFile(
    val file: Path
) {
    private companion object {
        // Magic headers
        const val SII_MAGIC_ENCRYPTED = "ScsC"
        const val SII_MAGIC_TEXT = "SiiN"
        const val SII_MAGIC_BINARY = "BSII"
        // The key used to encrypt/decrypt files with "ScsC" magic
        val AES_KEY = byteArrayOfInts(0x2A, 0x5F, 0xCB, 0x17, 0x91, 0xD2, 0x2F, 0xB6, 0x02, 0x45, 0xB3, 0xD8, 0x36, 0x9E, 0xD0, 0xB2, 0xC2, 0x73, 0x71, 0x56, 0x3F, 0xBF, 0x1F, 0x3C, 0x9E, 0xDF, 0x6B, 0x11, 0x82, 0x5A, 0x5D, 0x0A)
    }

    // Reads a game file and, if encrypted or binary, convert to plaintext
    fun read(): ByteArray {
        val fileContent = Files.readAllBytes(file)
        return unpack(fileContent)
    }

    // Parses a plaintext game file
    fun parse(callback: ParseCallback): Boolean {
        val data = read()
        val magic = getMagic(data)
        if (magic == SII_MAGIC_TEXT) { // Text
            return SiiTextParser.parse(data, callback)
        }
        else if (magic == SII_MAGIC_BINARY) {
            println("Unfortunately, binary saves aren't supported yet. Please create the save using a text format.")
            return false
        }
        return false
    }

    // Unpacks game file data and returns its plaintext version (regardless of whether it's encrypted or binary)
    private fun unpack(data: ByteArray): ByteArray {
        val magic = getMagic(data)
        if (magic == SII_MAGIC_TEXT || magic == SII_MAGIC_BINARY) { // Text
            return data
        }
        else if (magic == SII_MAGIC_ENCRYPTED) { // Encrypted
            // Useful data for unpacking, from byte 36 to end
            val subData = data.copyOfRange(36, data.size)
            // Initialization Vector, first 16 bytes
            val iv = subData.copyOfRange(0, 16)
            // Size of the inflated data, form byte 16 to 20, sizeof(int)
            val dataSizeByte = subData.copyOfRange(16, 16 + 4)
            // Obtain the integer value, it is in Little Endian byte order from C/C++
            val dataSize = ByteBuffer.wrap(dataSizeByte).order(ByteOrder.LITTLE_ENDIAN).int
            // Actually encrypted data, from byte 20 to end
            val cipherText = subData.copyOfRange(20, subData.size)
            // Decrypts the data
            val compressed = decrypt(AES_KEY, iv, cipherText)
            // Inflate the decrypted data
            return inflate(compressed, dataSize)
        }
        return ByteArray(0)
    }

    // Obtains the file discriminator String aka "magic"
    private fun getMagic(data: ByteArray): String {
        return String(data.copyOfRange(0, 4), StandardCharsets.UTF_8)
    }

    // Decrypts the data
    private fun decrypt(key: ByteArray, iv: ByteArray, data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(data)
    }

    // Inflates the data
    private fun inflate(data: ByteArray, dataSize: Int): ByteArray {
        val inflater = Inflater()
        inflater.setInput(data)
        val result = ByteArray(dataSize)
        inflater.inflate(result)
        inflater.end()
        return result
    }
}
