package me.dzikry.movapp.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import me.dzikry.movapp.R
import java.lang.Exception
import kotlin.math.roundToInt

class Tools {
    companion object {

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun saveToken(activity: Activity, token: String) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(Const.PREFERENCE_TOKEN, token)
                apply()
            }
        }

        fun restoreToken(activity: Activity): String? {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            return sharedPref.getString(Const.PREFERENCE_TOKEN, "")
        }

        fun getGridSpanCountMovie(activity: Activity): Int {
            val display: Display = activity.windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels.toFloat()
            val cellWidth = activity.resources.getDimension(R.dimen.item_movie_width)
            return (screenWidth / cellWidth).roundToInt()
        }

        fun dpToPx(c: Context, dp: Int): Int {
            val r = c.resources
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            ).roundToInt()
        }

        fun getHeightScreen(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }

        fun getWidthScreen(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        fun getSizeBottomNavBar(ctx: Context): Int {
            val resources = ctx.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                resources.getDimensionPixelSize(resourceId)
            } else 0
        }

        fun getStatusBarHeight(ctx: Context): Int {
            val resources = ctx.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                resources.getDimensionPixelSize(resourceId)
            } else 0
        }

        fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
            if (view.layoutParams is MarginLayoutParams) {
                val p = view.layoutParams as MarginLayoutParams
                p.setMargins(left, top, right, bottom)
                view.requestLayout()
            }
        }

        fun setStatusBarTransparent(act: Activity) {
            try {
                when (act.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        lightThemeStatusBar(act)
                    } // Night mode is not active, we're using the light theme
                    Configuration.UI_MODE_NIGHT_YES -> {
                        darkThemeStatusBar(act)
                    } // Night mode is active, we're using dark theme
                }
            } catch (e: Exception) {
                // light theme
                lightThemeStatusBar(act)
            }
        }

        private fun lightThemeStatusBar(act: Activity) {
            act.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            act.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                act.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                act.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            act.window.statusBarColor = Color.TRANSPARENT
        }

        private fun darkThemeStatusBar(act: Activity) {
            act.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            act.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            act.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            act.window.statusBarColor = Color.TRANSPARENT
        }

    }
}