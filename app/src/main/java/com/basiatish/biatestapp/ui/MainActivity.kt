package com.basiatish.biatestapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    }

    private fun controlNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tasksListFragment -> {
                    binding.screenName.text = resources.getText(R.string.tasks)
                    navigationBar.visibility = View.VISIBLE
                    binding.addSickList.visibility = View.GONE
                }
                R.id.taskDetailsFragment -> {
                    binding.screenName.text = resources.getText(R.string.task_details)
                    navigationBar.visibility = View.GONE
                    binding.addSickList.visibility = View.GONE
                }
                R.id.incidentFragment -> {
                    binding.screenName.text = resources.getText(R.string.incident)
                    navigationBar.visibility = View.GONE
                    binding.addSickList.visibility = View.GONE
                }
                R.id.taskDocumentsFragment -> {
                    binding.screenName.text = resources.getText(R.string.documents)
                    navigationBar.visibility = View.GONE
                    binding.addSickList.visibility = View.GONE
                }
                R.id.calendarFragment -> {
                    binding.screenName.text = resources.getText(R.string.timetable_days)
                    navigationBar.visibility = View.VISIBLE
                    binding.addSickList.visibility = View.GONE
                }
                R.id.profileFragment -> {
                    binding.screenName.text = resources.getText(R.string.profile)
                    navigationBar.visibility = View.VISIBLE
                    binding.addSickList.visibility = View.GONE
                }
                R.id.chatFragment -> {
                    binding.screenName.text = resources.getText(R.string.chat)
                    navigationBar.visibility = View.VISIBLE
                    binding.addSickList.visibility = View.GONE
                }
                R.id.sickListFragment -> {
                    binding.screenName.text = resources.getText(R.string.sick_list_title)
                    navigationBar.visibility = View.GONE
                    binding.addSickList.visibility = View.VISIBLE
                }
                R.id.addSickFragment -> {
                    binding.screenName.text = resources.getText(R.string.new_sick_item)
                    navigationBar.visibility = View.GONE
                    binding.addSickList.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}