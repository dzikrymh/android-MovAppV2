package me.dzikry.movapp.ui.news_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.databinding.FragmentNewsDetailBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class NewsDetailFragment : Fragment() {

    companion object {
        fun newInstance() = NewsDetailFragment()
    }

    private val viewModel: NewsDetailViewModel by viewModels()
    private lateinit var binding: FragmentNewsDetailBinding

    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val gson = Gson()
        val article = gson.fromJson(args.jsonArticle, Article::class.java)

        binding.news = article

        Glide.with(requireActivity())
            .load(article.urlToImage)
            .transform(CenterCrop())
            .into(binding.image)

        try {
            val dateApiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val date = dateApiFormat.parse(article.publishedAt)

            val dateView = SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH)

            date?.let {
                binding.publish.text = dateView.format(it).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.publish.text = article.publishedAt
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}