package me.dzikry.movapp.ui.search.news

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Article
import me.dzikry.movapp.data.networks.NewsAPIs
import me.dzikry.movapp.databinding.FragmentSearchNewsBinding
import me.dzikry.movapp.ui.search.news.adapter.SearchNewsAdapter
import me.dzikry.movapp.utils.PagingLoadStateAdapter

class SearchNewsFragment : Fragment() {

    companion object {
        fun newInstance() = SearchNewsFragment()
    }

    private lateinit var viewModel: SearchNewsViewModel
    private lateinit var binding: FragmentSearchNewsBinding

    private lateinit var mAdapter: SearchNewsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val api = NewsAPIs()
        val factory = SearchNewsViewModelFactory(api = api)
        viewModel = ViewModelProvider(this, factory)[SearchNewsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            back.setOnClickListener {
                findNavController().popBackStack()
            }

            mAdapter = SearchNewsAdapter { article -> showNewsDetail(article) }
            mAdapter.apply {
                recyclerViewSearch.adapter = withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(this),
                    footer = PagingLoadStateAdapter(this)
                )
            }

            edtSearch.setOnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    searchNews(edtSearch.text.toString().trim())
                }
                false
            }
        }
    }

    private fun searchNews(keyword: String) {
        lifecycleScope.launch {
            viewModel.getSearchNews(keyword = keyword).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun showNewsDetail(article: Article) {
        val gson = Gson()
        val jsonArticle = gson.toJson(article)

        val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToNewsDetailFragment(jsonArticle)
        findNavController().navigate(action)
    }

}