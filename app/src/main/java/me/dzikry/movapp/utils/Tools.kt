package me.dzikry.movapp.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import me.dzikry.movapp.R
import kotlin.math.roundToInt

class Tools {
    companion object {

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

    }
}