package com.application.zaki.movies.presentation.container

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.ActivityContainerBinding
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
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
            if (destination.label == "fragment_detail" || destination.label == "fragment_list" || destination.label == "fragment_list_discover" || destination.label == "fragment_splash_screen") {
                binding.bottomNavigationView.gone()
            } else {
                binding.bottomNavigationView.visible()
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}