package me.dzikry.movapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.R
import me.dzikry.movapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivity = this

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    companion object {
        lateinit var mainActivity: MainActivity

        fun toastTop(message: String, textColor: Int, bg: Int) {
            val card: CardView = mainActivity.findViewById(R.id.toastContainer)
            val text: TextView = mainActivity.findViewById(R.id.toastText)

            card.setCardBackgroundColor(bg)
            text.setTextColor(textColor)
            text.text = message

            animateToast(true)
            Handler().postDelayed({
                animateToast(false)
            }, 2000)
        }

        private fun animateToast(visible: Boolean) {
            val card: CardView = mainActivity.findViewById(R.id.toastContainer)
            val moveY = if (visible) 2 * card.height else 0
            card.animate()
                .translationY(moveY.toFloat())
                .setStartDelay(100)
                .setDuration(400)
                .start()
        }
    }
}