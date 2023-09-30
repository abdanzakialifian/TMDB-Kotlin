package com.application.zaki.movies.presentation.setting

import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentSettingBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment

class SettingFragment : BaseVBFragment<FragmentSettingBinding>(),
    CompoundButton.OnCheckedChangeListener {
    override fun getViewBinding(): FragmentSettingBinding =
        FragmentSettingBinding.inflate(layoutInflater)

    override fun initView() {
        binding?.apply {
            switchTheme.setOnCheckedChangeListener(this@SettingFragment)
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        binding?.apply {
            if (isChecked) {
                imgTheme.setImageResource(R.drawable.ic_light_mode_28)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                imgTheme.setImageResource(R.drawable.ic_night_mode_28)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}