package com.hkbufyp.hrms.data.repository

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt.CryptoObject
import com.hkbufyp.hrms.domain.constant.BiometricConstant
import com.hkbufyp.hrms.domain.model.AndroidKeyStatus
import com.hkbufyp.hrms.domain.model.BiometricStatus
import com.hkbufyp.hrms.domain.repository.BiometricRepository
import com.hkbufyp.hrms.domain.repository.UserPreferencesRepository
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.random.Random

class BiometricRepositoryImpl(
    private val biometricManager: BiometricManager,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
): BiometricRepository {

    private fun getKeyStore(): KeyStore =
        KeyStore.getInstance(BiometricConstant.KEY_STORE).apply {
            load(null)
        }

    private fun isKeyExist(): Boolean =
        getKeyStore().isKeyEntry(BiometricConstant.KEY_ALIAS)

    private suspend fun isTokenExist(): Boolean {
        val token = userPreferencesRepository.getBiometricIV().first()
        val iv = userPreferencesRepository.getBiometricToken().first()

        return !token.isEmptyOrBlank() && !iv.isEmptyOrBlank()
    }

    private fun genAndroidKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, BiometricConstant.KEY_STORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            BiometricConstant.KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setKeySize(256)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getAndroidKeyStatus(): AndroidKeyStatus {
        if (isKeyExist()) {
            return try {
                val byteArray = ByteArray(16)
                Random.nextBytes(byteArray)
                getCryptoObjectInternal(false, byteArray)
                AndroidKeyStatus.VALID
            } catch (e: KeyPermanentlyInvalidatedException) {
                AndroidKeyStatus.INVALIDATED
            } catch (e: Exception) {
                AndroidKeyStatus.ERROR
            }
        } else {
            return try {
                genAndroidKey()
                AndroidKeyStatus.VALID
            } catch (e: Exception) {
                AndroidKeyStatus.ERROR
            }
        }
    }

    override suspend fun clean() {
        val keyStore = getKeyStore()
        if (keyStore.isKeyEntry(BiometricConstant.KEY_ALIAS)) {
            keyStore.deleteEntry(BiometricConstant.KEY_ALIAS)
        }
        userPreferencesRepository.saveBiometricToken("")
        userPreferencesRepository.saveBiometricIV("")
    }

    private fun getSecretKey(): SecretKey =
        getKeyStore().getKey(BiometricConstant.KEY_ALIAS, null) as SecretKey

    private fun getCipher(): Cipher {
        return Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES +
                    "/" + KeyProperties.BLOCK_MODE_CBC +
                    "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

    private fun isBiometricAvailable(): Boolean {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    private fun getCryptoObjectInternal(isEncrypt: Boolean, iv: ByteArray?): CryptoObject {
        val cipher = getCipher()
        val secretKey = getSecretKey()

        if (isEncrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        }

        return CryptoObject(cipher)
    }

    override suspend fun getCryptoObject(isEncrypt: Boolean): CryptoObject {
        return if (isEncrypt) {
            getCryptoObjectInternal(true, null)
        } else {
            val ivStr = userPreferencesRepository.getBiometricIV().first()
            val iv = Base64.decode(ivStr, Base64.DEFAULT)
            getCryptoObjectInternal(false, iv)
        }
    }

    override fun getBiometricStatus() =
        flow {
            val biometricStatus = BiometricStatus(
                isBiometricTokenExist = isTokenExist(),
                isBiometricAvailable = isBiometricAvailable(),
                keyStatus = getAndroidKeyStatus()
            )

            println(biometricStatus)
            if (biometricStatus.isKeyInvalidated()) {
                clean()
            }

            emit(biometricStatus)
        }

    override suspend fun getEncryptedToken(id: String, cryptoObject: CryptoObject) {
        userRepository.updateUserBiometric(id).collect {
            when (it) {
                is NetworkResponse.Success -> {
                    val cipher = cryptoObject.cipher!!
                    val encrypted = cipher.doFinal(it.data?.toByteArray(Charsets.UTF_8))
                    val base64 = Base64.encodeToString(encrypted, Base64.DEFAULT)
                    val iv = cipher.iv!!
                    val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)
                    userPreferencesRepository.saveBiometricToken(base64)
                    userPreferencesRepository.saveBiometricIV(ivBase64)
                }
                is NetworkResponse.Failure -> {
                    println(it.errMessage)
                }
            }
        }
    }

    override fun decryptToken(cryptoObject: CryptoObject) =
        flow {
            userPreferencesRepository.getBiometricToken().collect { encToken ->
                val data = Base64.decode(encToken, Base64.DEFAULT)
                val decoded = cryptoObject.cipher?.doFinal(data)
                val token = decoded?.toString(Charsets.UTF_8) ?: ""
                emit(token)
            }
        }

}