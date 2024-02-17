package com.application.tmdb.presentation.splash

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.application.tmdb.databinding.FragmentSplashScreenBinding
import com.application.tmdb.presentation.base.BaseVBFragment

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseVBFragment<FragmentSplashScreenBinding>() {

    override fun getViewBinding(): FragmentSplashScreenBinding =
        FragmentSplashScreenBinding.inflate(layoutInflater)

    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            val navigateToHomeFragment = SplashScreenFragmentDirections.actionSplashScreenFragmentToMovieFragment()
            findNavController().navigate(navigateToHomeFragment)
        }, DELAY_SPLASH_SCREEN)
    }

    companion object {
        private const val DELAY_SPLASH_SCREEN = 3000L
    }
}