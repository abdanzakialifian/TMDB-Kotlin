package com.application.tmdb.setting.ui

import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.application.tmdb.common.R
import com.application.tmdb.common.base.BaseVBFragment
import com.application.tmdb.common.utils.RxDisposer
import com.application.tmdb.setting.databinding.FragmentSettingBinding
import com.application.tmdb.setting.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseVBFragment<FragmentSettingBinding>(),
    CompoundButton.OnCheckedChangeListener {

    private val settingViewModel by viewModels<SettingViewModel>()

    override fun getViewBinding(): FragmentSettingBinding =
        FragmentSettingBinding.inflate(layoutInflater)

    override fun initView() {
        binding?.apply {
            switchTheme.setOnCheckedChangeListener(this@SettingFragment)
        }
        settingViewModel.getTMDBTheme(RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) })
        observeData()
    }

    private fun observeData() {
        settingViewModel.tmdbTheme.observe(viewLifecycleOwner) { isDarkMode ->
            changeTheme(isDarkMode)
        }
    }

    private fun changeTheme(isChecked: Boolean) {
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

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        changeTheme(isChecked)
        settingViewModel.saveTMDBTheme(isChecked)
    }
}