package com.yoon.paymenttest

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.yoon.paymenttest.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 스위치 on/off
        when (SavedData.systemMode) {
            true -> {
                mBinding.darkModeSwitch.isChecked = true
            }
            false -> {
                mBinding.darkModeSwitch.isChecked = false
            }
        }

        mBinding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    SavedData.systemMode = true
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SavedData.systemMode = false
            }
        }

        with (mBinding) {
            btnOneTime.setOnClickListener {
                findNavController().navigate(R.id.action_fragSettings_to_fragAD)
            }
            btnSubscription.setOnClickListener {
                Toast.makeText(activity,"정기결제 클릭",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getViewBinding(): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(layoutInflater)
    }
}