package com.mumayank.howstheweather.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.databinding.ActivityMainBinding
import com.mumayank.howstheweather.help.HelpActivity
import com.mumayank.howstheweather.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupAppropriateNavigation()
    }

    private fun setupAppropriateNavigation() {
        if (binding.bottomNavigationView != null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            NavigationUI.setupWithNavController(
                binding.bottomNavigationView!!,
                navHostFragment.navController
            )
        } else {
            if (binding.drawerLayout != null) {
                val toggle = ActionBarDrawerToggle(
                    this, binding.drawerLayout, binding.toolbar,
                    R.string.app_name,
                    R.string.app_name
                )
                binding.drawerLayout!!.addDrawerListener(toggle)
                toggle.syncState()
            }
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            binding.navView!!.setupWithNavController(navHostFragment.navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_help -> {
                startActivity(Intent(this, HelpActivity::class.java))
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}