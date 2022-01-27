package me.dzikry.movapp.ui.search.news

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.hideKeyboard

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

        Tools.setStatusBarTransparent(requireActivity())
        Tools.setMargins(
            binding.back,
            left = Tools.dpToPx(requireContext(), 16),
            top = 0,
            right = 0,
            bottom = 0
        )
        Tools.setMargins(
            binding.cardViewSearch,
            left = Tools.dpToPx(requireContext(), 16),
            top = Tools.dpToPx(requireContext(), 16) + Tools.getStatusBarHeight(requireContext()),
            right = Tools.dpToPx(requireContext(), 16),
            bottom = 0
        )

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
        view?.let { context?.hideKeyboard(it) }
        lifecycleScope.launch {
            viewModel.getSearchNews(keyword = keyword).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun showNewsDetail(article: Article) {
//        val gson = Gson()
//        val jsonArticle = gson.toJson(article)
//
//        val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToNewsDetailFragment(jsonArticle)
//        findNavController().navigate(action)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(article.url)
        startActivity(intent)
    }

}