package com.mx.crystalcloud.bookodemia.extra

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mx.crystalcloud.bookodemia.R
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun encrypt(context: Context, strToEncrypt: String): ByteArray {
    val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)
    val keygen = KeyGenerator.getInstance("AES")
    keygen.init(256)
    val key = keygen.generateKey()
    saveSecretKey(context, key)
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val cipherText = cipher.doFinal(plainText)
    saveInitializationVector(context, cipher.iv)

    val sb = StringBuilder()
    for (b in cipherText) {
        sb.append(b.toChar())
    }
    //Toast.makeText(context, "dbg encrypted = [" + sb.toString() + "]", Toast.LENGTH_LONG).show()

    return cipherText
}

fun decrypt(context: Context, dataToDecrypt: ByteArray): ByteArray {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val ivSpec = IvParameterSpec(getSavedInitializationVector(context))
    cipher.init(Cipher.DECRYPT_MODE, getSavedSecretKey(context), ivSpec)
    val cipherText = cipher.doFinal(dataToDecrypt)

    val sb = StringBuilder()
    for (b in cipherText) {
        sb.append(b.toChar())
    }
    //Toast.makeText(context, "dbg decrypted = [" + sb.toString() + "]", Toast.LENGTH_LONG).show()

    return cipherText
}

fun saveSecretKey(context: Context, secretKey: SecretKey) {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(secretKey)
    val strToSave =
        String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPref.edit()
    editor.putString("secret_key", strToSave)
    editor.apply()
}

fun getSavedSecretKey(context: Context): SecretKey {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val strSecretKey = sharedPref.getString("secret_key", "")
    val bytes = android.util.Base64.decode(strSecretKey, android.util.Base64.DEFAULT)
    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
    val secretKey = ois.readObject() as SecretKey
    return secretKey
}

fun saveInitializationVector(context: Context, initializationVector: ByteArray) {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(initializationVector)
    val strToSave =
        String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPref.edit()
    editor.putString("initialization_vector", strToSave)
    editor.apply()
}

fun getSavedInitializationVector(context: Context): ByteArray {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    val strInitializationVector = sharedPref.getString("initialization_vector", "")
    val bytes = android.util.Base64.decode(strInitializationVector, android.util.Base64.DEFAULT)
    val ois = ObjectInputStream(ByteArrayInputStream(bytes))
    val initializationVector = ois.readObject() as ByteArray
    return initializationVector
}

fun popUp(
    activity: Activity,
    message: String
) {
    val snackBar =
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    view.layoutParams = params
    snackBar.show()
}

fun saveSesion(context: Context, jsonObject: JSONObject) {
    val sharePreferences = getPreferences(context)
    with(sharePreferences.edit()) {
        putString("token", jsonObject.getString(context.getString(R.string.key_token)))
        apply()
    }
}

fun dropSession(context: Context){
    val sharedPreferences = getPreferences(context)
    with(sharedPreferences.edit()){
        clear()
        apply()
    }
}

fun getSession(context: Context, clave: String): String{
    val sharedPreferences = getPreferences(context)
    return sharedPreferences.getString(clave,"").toString()
}

fun getPreferences(context: Context): SharedPreferences {
    return EncryptedSharedPreferences.create(
        context.getString(
            R.string.name_file_preferences
        ),
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

public fun fieldValidation(
    type: String,
    tiet: TextInputEditText,
    til: TextInputLayout,
    context: Context
): Boolean {
    var ok = true;
    if (tiet.text.toString().isEmpty()) {
        ok = false
    } else {
        when (type) {
            in "email" ->
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(tiet.text.toString())
                        .matches()
                ) {
                    til.isErrorEnabled = false
                } else {
                    til.error = context.getString(R.string.wrong_email)
                    false
                }
            else -> {
                println("ok")
            }
        }

    }
    return ok
}

fun sessionValidator(context: Context): Boolean{
    val sharedPreferences = getPreferences(context)
    val token = sharedPreferences.getString("token","")
    return !token.isNullOrEmpty()
}
