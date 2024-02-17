package com.application.tmdb.presentation.container

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.application.tmdb.R
import com.application.tmdb.databinding.ActivityContainerBinding
import com.application.tmdb.common.gone
import com.application.tmdb.common.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContainerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set bottom navigation with navigation component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label == "fragment_detail" || destination.label == "fragment_movie_tv_show" ||
                destination.label == "fragment_splash_screen" || destination.label == "fragment_detail_cast_crew"
            ) {
                binding.bottomNavigationView.gone()
            } else {
                binding.bottomNavigationView.visible()
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}