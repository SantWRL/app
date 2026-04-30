package br.ufpi.lgpd.educacional.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import br.ufpi.lgpd.educacional.R
import br.ufpi.lgpd.educacional.databinding.ActivityMainBinding
import br.ufpi.lgpd.educacional.ui.onboarding.OnboardingActivity
import timber.log.Timber

/**
 * MainActivity - Tela principal do app com navegação
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar se é primeira vez do usuário
        if (isFirstTime()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup com bottom navigation
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        // Listener para mudança de fragmentos
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = when (destination.id) {
                R.id.homeFragment -> getString(R.string.home_title)
                R.id.lessonsFragment -> getString(R.string.home_lessons)
                R.id.quizzesFragment -> getString(R.string.home_quizzes)
                R.id.profileFragment -> getString(R.string.profile_title)
                R.id.quizDetailFragment -> getString(R.string.quiz_results)
                else -> getString(R.string.home_title)
            }
            binding.bottomNavigation.visibility = if (destination.id == R.id.quizDetailFragment) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    private fun isFirstTime(): Boolean {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return !sharedPref.getBoolean("has_opened_before", false)
            .also {
                if (!it) {
                    sharedPref.edit().putBoolean("has_opened_before", true).apply()
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

// Placeholder para BuildConfig
object BuildConfig {
    const val DEBUG = true
}
