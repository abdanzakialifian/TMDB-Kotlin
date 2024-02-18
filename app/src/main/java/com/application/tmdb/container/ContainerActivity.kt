package com.application.tmdb.container

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.application.tmdb.R
import com.application.tmdb.common.utils.Constant
import com.application.tmdb.common.utils.gone
import com.application.tmdb.common.utils.visible
import com.application.tmdb.databinding.ActivityContainerBinding
import com.application.tmdb.navigation.DetailScreenNavigation
import com.application.tmdb.navigation.ListScreenNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContainerActivity : AppCompatActivity(), DetailScreenNavigation, ListScreenNavigation {
    private lateinit var binding: ActivityContainerBinding

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set bottom navigation with navigation component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label == Constant.FRAGMENT_DETAIL_DESTINATION || destination.label == Constant.FRAGMENT_MOVIE_TV_SHOW_DESTINATION ||
                destination.label == Constant.FRAGMENT_SPLASH_SCREEN_DESTINATION || destination.label == Constant.FRAGMENT_DETAIL_CAST_CREW_DESTINATION
            ) {
                binding.bottomNavigationView.gone()
            } else {
                binding.bottomNavigationView.visible()
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController!!)
    }

    override fun launchDetailScreen(detailScreenArguments: DetailScreenNavigation.DetailScreenArguments) {
        val bundle = bundleOf(
            Constant.KEY_ID to detailScreenArguments.id,
            Constant.KEY_INTENT_FROM to detailScreenArguments.intentFrom,
        )
        navController?.navigate(R.id.detail_fragment, bundle)
    }

    override fun launchListScreen(listScreenArguments: ListScreenNavigation.ListScreenArguments) {
        val bundle = bundleOf(
            Constant.KEY_INTENT_FROM to listScreenArguments.intentFrom,
            Constant.KEY_MOVIE to listScreenArguments.movie,
            Constant.KEY_TV_SHOW to listScreenArguments.tvShow,
        )
        navController?.navigate(R.id.movie_tv_show_fragment, bundle)
    }
}