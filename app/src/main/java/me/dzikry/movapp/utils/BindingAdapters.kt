package me.dzikry.movapp.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import me.dzikry.movapp.R
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("loadUrl")
fun ImageView.loadUrl(url: String?) {
    if (url.isNullOrEmpty()) return
    Glide.with(this).load(url).transform(CenterCrop()).into(this)
}

@BindingAdapter("publishAuthor")
fun TextView.publishAuthor(publishAt: String?) {
    if (publishAt.isNullOrEmpty()) return
    try {
        val dateApiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val date = dateApiFormat.parse(publishAt)

        val dateView = SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH)

        date?.let {
            text = dateView.format(it).toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        text = publishAt
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("releaseMovie")
fun TextView.releaseMovie(release: String?) {
    if (release.isNullOrEmpty()) return
    text = try {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(release)
        SimpleDateFormat("dd MMMM yyyy").format(date!!)
    } catch (e: Exception) {
        release
    }
}

@BindingAdapter("rate")
fun TextView.rate(rating: Double?) {
    text = rating?.toString() ?: "0.0"
}

@BindingAdapter("loadProfile")
fun ImageView.loadProfile(url: String?) {
    if (url.isNullOrEmpty()) {
        setImageResource(R.drawable.ic_account)
    } else {
        val newUrl = StringBuilder(url)
        if (newUrl.indexOf("http") == -1) {
            newUrl.insert(0, Const.BASE_PATH_AVATAR)
        } else {
            newUrl.deleteCharAt(0)
        }
        Glide.with(this)
            .load(newUrl.toString())
            .transform(CenterCrop())
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    setImageResource(R.drawable.ic_account)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(this)
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("dateReview")
fun TextView.dateReview(updatedAt: String?) {
    if (updatedAt.isNullOrEmpty()) return
    text = try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val dt = formatter.format(parser.parse(updatedAt)!!)
        dt
    } catch (e: Exception) {
        updatedAt
    }
}

@BindingAdapter("html")
fun TextView.html(content: String?) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(content)
    }
}

@BindingAdapter("fillStar")
fun RatingBar.fillStar(rate: Float?) {
    rating = (rate?.div(2) ?: 0) as Float
}

@SuppressLint("SetTextI18n")
@BindingAdapter("username")
fun TextView.username(username: String?) {
    if (username.isNullOrEmpty()) return
    text = "@${username}"
}