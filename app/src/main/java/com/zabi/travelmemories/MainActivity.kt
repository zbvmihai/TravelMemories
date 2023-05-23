package com.zabi.travelmemories

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.zabi.travelmemories.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        checkTheme()
        applyLanguage()

        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_about,
                R.id.nav_contact,
                R.id.nav_share,
                R.id.nav_settings,
                R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val logOutItem: MenuItem = navView.menu.findItem(R.id.nav_logout)
        val shareItem: MenuItem = navView.menu.findItem(R.id.nav_share)
        logOutItem.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    Toast.makeText(this, "User Logged Out!", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        delay(500)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }

        shareItem.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.nav_share -> {
                    val appPackageName = packageName
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out this awesome app: https://play.google.com/store/apps/details?id=$appPackageName"
                        )
                    }
                    startActivity(Intent.createChooser(intent, "Share via"))
                    true
                }
                else -> false
            }
        }
    }

    private fun checkTheme() {
        val isDarkModeEnabled = sharedPrefs.getBoolean("isDarkModeEnabled", false)
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun applyLanguage(){
        val res: Resources = resources
        val config: Configuration = res.configuration
        val locale = Locale(res.configuration.locales.get(0).language)
        config.setLocale(locale)
        res.updateConfiguration(config, res.displayMetrics)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}