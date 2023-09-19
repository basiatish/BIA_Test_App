package com.basiatish.biatestapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var navigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment

        navController = navHostFragment.navController

        navigationBar = binding.navBar
        navigationBar.setupWithNavController(navController)
        controlNavigation()
        checkPermission()
    }

    private fun controlNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tasksListFragment -> {
                    binding.screenName.text = resources.getText(R.string.tasks)
                    navigationBar.visibility = View.VISIBLE
                }
                R.id.taskDetailsFragment -> {
                    binding.screenName.text = resources.getText(R.string.task_details)
                    navigationBar.visibility = View.GONE
                }
                R.id.incidentFragment -> {
                    binding.screenName.text = resources.getText(R.string.incident)
                    navigationBar.visibility = View.GONE
                }
                R.id.taskDocumentsFragment -> {
                    binding.screenName.text = resources.getText(R.string.documents)
                    navigationBar.visibility = View.GONE
                }
            }
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}