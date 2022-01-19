package me.dzikry.movapp.ui.home.news

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.networks.NewsAPIs
import me.dzikry.movapp.data.repositories.NewsRepository
import me.dzikry.movapp.databinding.FragmentNewsBinding
import me.dzikry.movapp.ui.home.news.adapter.NewsAdapter
import me.dzikry.movapp.ui.home.news.adapter.TrendingAdapter
import me.dzikry.movapp.utils.Resource

class NewsFragment : Fragment() {

    companion object {
        fun newInstance() = NewsFragment()
    }

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding

    private lateinit var adapterTrending: TrendingAdapter
    private lateinit var adapterCategory: NewsAdapter

    private val newsCategoryPage = 1
    private val newsTrendingPage = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val api = NewsAPIs()
        val repository = NewsRepository(api)
        val factory = NewsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            edtSearch.setOnClickListener {
                val action = NewsFragmentDirections.actionNewsFragmentToSearchNewsFragment()
                findNavController().navigate(action)
            }

            radioGroup.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.radioBusiness -> {
                        getNewsCategory("business", newsCategoryPage)
                    }
                    R.id.radioEntertainment -> {
                        getNewsCategory("entertainment", newsCategoryPage)
                    }
                    R.id.radioGeneral -> {
                        getNewsCategory("general", newsCategoryPage)
                    }
                    R.id.radioHealth -> {
                        getNewsCategory("health", newsCategoryPage)
                    }
                    R.id.radioScience -> {
                        getNewsCategory("science", newsCategoryPage)
                    }
                    R.id.radioSports -> {
                        getNewsCategory("sports", newsCategoryPage)
                    }
                    else -> {
                        getNewsCategory("technology", newsCategoryPage)
                    }
                }
            }

            getNewsCategory("business", newsCategoryPage)
            getNewsTrending(newsTrendingPage)
        }
    }

    private fun getNewsCategory(category: String, page: Int) {
        viewModel.getHeadlinesNews(category, page)
        viewModel.headlineNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingCategory(false)
                    response.data?.let {
                        adapterCategory = NewsAdapter {news -> showNewsDetails(news)}
                        binding.recyclerViewCategory.apply {
                            adapter = adapterCategory
                        }
                        adapterCategory.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    isLoadingCategory(false)
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    isLoadingCategory(true)
                }
            }
        })
    }

    private fun isLoadingCategory(bool: Boolean) {
        binding.apply {
            shimmerCategory.visibility = if (bool) View.VISIBLE else View.GONE
            recyclerViewCategory.visibility = if (bool) View.GONE else View.VISIBLE
        }
    }

    private fun getNewsTrending(page: Int) {
        viewModel.getTrendingNews(page)
        viewModel.trendingNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingTrending(false)
                    response.data?.let {
                        adapterTrending = TrendingAdapter {news -> showNewsDetails(news)}
                        binding.recyclerViewTrending.apply {
                            adapter = adapterTrending
                        }
                        adapterTrending.differ.submitList(it)
                    }
                }
                is Resource.Error -> {
                    isLoadingTrending(false)
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    isLoadingTrending(true)
                }
            }
        })
    }

    private fun isLoadingTrending(bool: Boolean) {
        binding.apply {
            shimmerTrending.visibility = if (bool) View.VISIBLE else View.GONE
            recyclerViewTrending.visibility = if (bool) View.GONE else View.VISIBLE
        }
    }

    private fun showNewsDetails(news: Article) {
        val gson = Gson()
        val jsonArticle : String = gson.toJson(news)

        val action = NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(jsonArticle)
        findNavController().navigate(action)
    }

}