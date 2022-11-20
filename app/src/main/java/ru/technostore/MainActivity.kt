package ru.technostore

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.technostore.databinding.ActivityMainBinding
import ru.technostore.utils.ConnectivityObserver
import ru.technostore.utils.ConnectivityReceiver
import java.lang.IllegalArgumentException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment

    private var dialog: Dialog? = null

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
                R.id.cartFragment -> hideBottomNavigation()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this@MainActivity
    }


    private fun hideBottomNavigation() {
        binding.bottomNavigation.isVisible = false
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.isVisible = true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showDialog(isConnected)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
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


    private fun showDialog(isConnected: Boolean) {
        if (!isConnected) {
            dialog = Dialog(this)
            dialog?.setContentView(R.layout.dialog_check_connection)
            dialog?.setCancelable(false)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
        } else {
            dialog?.dismiss()
        }
    }
}