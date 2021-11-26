package com.yoon.paymenttest

import android.annotation.SuppressLint
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@SuppressLint("StaticFieldLeak")
object SavedData {
    private lateinit var masterkey : MasterKey
    private lateinit var sharedPreferences: EncryptedSharedPreferences

    fun init(context: Context) {
        masterkey = MasterKey.Builder(context,MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        sharedPreferences = EncryptedSharedPreferences.create(context, "book", masterkey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM) as EncryptedSharedPreferences
    }

    var systemMode: Boolean
        get() = sharedPreferences.getBoolean(Key.SYSTEM_MODE, false)
        set(value) = sharedPreferences.edit().putBoolean(Key.SYSTEM_MODE, value).apply()

    var crystal: Int
        get() = sharedPreferences.getInt(Key.PREF_KEY_CRYSTAL, 0)
        set(value) = sharedPreferences.edit().putInt(Key.PREF_KEY_CRYSTAL, value).apply()

}