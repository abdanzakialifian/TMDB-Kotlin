package com.application.tmdb.splash

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.application.tmdb.common.base.BaseVBFragment
import com.application.tmdb.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseVBFragment<FragmentSplashScreenBinding>() {

    override fun getViewBinding(): FragmentSplashScreenBinding =
        FragmentSplashScreenBinding.inflate(layoutInflater)

    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            val navigateToMovieFragment =
                SplashScreenFragmentDirections.actionSplashScreenFragmentToMovieFragment()
            findNavController().navigate(navigateToMovieFragment)
        }, DELAY_SPLASH_SCREEN)
    }

    companion object {
        private const val DELAY_SPLASH_SCREEN = 3000L
    }
}