package ru.technostore

import android.os.Bundle
import android.view.Menu
import android.widget.FrameLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.BaselineLayout
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import iwir.oerit.bottomnav.ext.SmoothBottomBar
import ru.technostore.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        setupSmoothBottomMenu()


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.splashFragment -> hideBottomNavigation()
                R.id.homeFragment -> showBottomNavigation()
                R.id.productDetailsFragment -> hideBottomNavigation()
            }
        }
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigation.isVisible = false
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.isVisible = true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //set an active fragment programmatically
    fun setSelectedItem(pos:Int){
        binding.bottomNavigation.setSelectedItem(pos)
    }
    //set badge indicator
    fun setBadge(pos:Int){
        binding.bottomNavigation.setBadge(pos)
    }
    //remove badge indicator
    fun removeBadge(pos:Int){
        binding.bottomNavigation.removeBadge(pos)
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu)
        val menu = popupMenu.menu
        //binding.bottomBar.setupWithNavController(menu, navController)
        binding.bottomNavigation.setupWithNavController( navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}