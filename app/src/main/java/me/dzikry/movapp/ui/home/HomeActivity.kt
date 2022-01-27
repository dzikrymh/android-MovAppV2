package me.dzikry.movapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.databinding.ActivityHomeBinding
import me.dzikry.movapp.ui.home.movie.MovieFragment
import me.dzikry.movapp.utils.Const
import java.io.Serializable

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeActivity = this

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.navView.setupWithNavController(navController)
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (navHostFragment.childFragmentManager.backStackEntryCount > 0) {
            if (navHostFragment.childFragmentManager.fragments[0] is MovieFragment) {
                if (!doubleBackToExitPressedOnce) {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this, getString(R.string.press_back_again), Toast.LENGTH_SHORT).show()
                    Handler(Looper.myLooper()!!).postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                } else {
                    finish()
                }
            } else {
                navController.popBackStack()
            }
        } else if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this, getString(R.string.press_back_again), Toast.LENGTH_SHORT).show()
            Handler(Looper.myLooper()!!).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        } else {
            super.onBackPressed()
            return
        }
    }

    companion object {
        lateinit var homeActivity: HomeActivity

        fun animate(hide: Boolean) {
            val bottom: BottomNavigationView = homeActivity.findViewById(R.id.nav_view)
            val moveY = if (hide) 3 * bottom.height else 0
            bottom.animate()
                .translationY(moveY.toFloat())
                .setStartDelay(100)
                .setDuration(400)
                .start()
        }
    }
}