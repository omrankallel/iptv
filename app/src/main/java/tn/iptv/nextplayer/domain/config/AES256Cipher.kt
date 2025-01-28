package tn.iptv.nextplayer.domain.config

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES256Cipher {

    private const val AES_KEY = "22119931435261221144ACBACCDAEFEF" // Clé de 32 caractères (256 bits)
    private const val AES_IV = "2210213141515539" // IV de 16 caractères (128 bits)

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(AES_KEY.toByteArray(Charsets.UTF_8), "AES")
        val ivParameterSpec = IvParameterSpec(AES_IV.toByteArray(Charsets.UTF_8))
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    fun decrypt(encryptedData: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(AES_KEY.toByteArray(Charsets.UTF_8), "AES")
        val ivParameterSpec = IvParameterSpec(AES_IV.toByteArray(Charsets.UTF_8))
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.NO_WRAP))
        return String(decryptedBytes, Charsets.UTF_8)
    }

    fun md5(input: String): String {
        return MessageDigest.getInstance("MD5").digest(input.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }
}
