package com.yoon.paymenttest

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.yoon.paymenttest.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {

    private val This: ActivityMain = this

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        SavedData.init(This)
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        try {
            // 툴바
            mBinding.apply {
                setContentView(root)
                initNavigation()
                setSupportActionBar(toolbar)
                val navHostFragment: NavHostFragment =
                    supportFragmentManager.findFragmentById(R.id.navi_host) as NavHostFragment
                val navController = navHostFragment.navController
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.fragSettings
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
            }
        } catch (e: Exception) {
            Log.e("checkCheck", e.message.toString())
        }

        // 다크모드-시스템모드
        when (SavedData.systemMode) {
            true -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initNavigation() {
//        NavigationUI.setupWithNavController(
//            mBinding.navigation,
//            findNavController(R.id.navi_host)
//        )
    }
}