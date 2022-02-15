package me.dzikry.movapp.ui.home

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.dzikry.movapp.R
import me.dzikry.movapp.databinding.ActivityHomeBinding
import me.dzikry.movapp.ui.home.movie.MovieFragment
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.utils.Const

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var account: Account
    private lateinit var pathPhoto: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            it.apply {
                val jsonAccount = getString(Const.ACCOUNT)

                val gson = Gson()
                account = gson.fromJson(jsonAccount, Account::class.java)

                account.avatar.tmdb?.avatar_path?.let { path ->
                    pathPhoto = Const.BASE_PATH_TMDB + path
                } ?: run {
                    pathPhoto = Const.BASE_PATH_AVATAR + account.avatar.gravatar.hash
                }
            }
        }

        homeActivity = this

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.navView.setupWithNavController(navController)

        loadUrlAsTabProfileImage(pathPhoto)
    }

    private fun loadUrlAsTabProfileImage(url: String) {
        binding.navView.itemIconTintList = null
        Glide.with(this@HomeActivity)
            .asBitmap()
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val drawable: Drawable = BitmapDrawable(resources, resource)
                    binding.navView.menu.findItem(R.id.profileFragment).icon = drawable
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    binding.navView.menu.findItem(R.id.profileFragment).icon = resources.getDrawable(R.drawable.ic_account)
                }
            })
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