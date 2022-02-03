package me.dzikry.movapp.ui.search.movie

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.databinding.FragmentSearchMovieBinding
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.PagingLoadStateAdapter
import me.dzikry.movapp.ui.search.movie.adapter.SearchMovieAdapter
import me.dzikry.movapp.utils.SpacingItemDecoration
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.hideKeyboard

class SearchMovieFragment : Fragment() {

    companion object {
        fun newInstance() = SearchMovieFragment()
    }

    private lateinit var viewModel: SearchMovieViewModel
    private lateinit var binding: FragmentSearchMovieBinding

    private lateinit var mAdapter: SearchMovieAdapter
    private lateinit var moviesLayoutMgr: StaggeredGridLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val api = MovieAPIs()
        val factory = SearchMovieViewModelFactory(api = api)
        viewModel = ViewModelProvider(this, factory)[SearchMovieViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMovieBinding.inflate(inflater, container, false)

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
        HomeActivity.animate(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        moviesLayoutMgr = StaggeredGridLayoutManager(
            Tools.getGridSpanCountMovie(requireActivity()),
            StaggeredGridLayoutManager.VERTICAL
        )
        mAdapter = SearchMovieAdapter { movie -> showDetailMovie(movie) }
        binding.apply {
            recyclerViewSearch.layoutManager = StaggeredGridLayoutManager(Tools.getGridSpanCountMovie(requireActivity()), StaggeredGridLayoutManager.VERTICAL)
            val decoration = SpacingItemDecoration(Tools.getGridSpanCountMovie(requireActivity()), Tools.dpToPx(requireContext(), 2), false)
            recyclerViewSearch.addItemDecoration(decoration)
            mAdapter.apply {
                recyclerViewSearch.adapter = withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(this),
                    footer = PagingLoadStateAdapter(this)
                )
                addLoadStateListener { loadState ->
                    if (loadState.refresh is LoadState.Loading) {
                        isLoadingMovie(true)
                    } else if (loadState.refresh is LoadState.NotLoading) {
                        isLoadingMovie(false)
                    }
                    if (loadState.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        mAdapter.itemCount < 1) {
                        binding.movieNotFound.visibility = View.VISIBLE
                    }
                }
            }

            back.setOnClickListener {
                findNavController().popBackStack()
            }

            edtSearch.setOnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    searchMovie(edtSearch.text.toString().trim())
                }
                false
            }
        }
    }

    private fun isLoadingMovie(bool: Boolean) {
        binding.movieNotFound.visibility = View.GONE
        binding.shimmerSearchMovie.visibility = if (bool) View.VISIBLE else View.GONE
        binding.recyclerViewSearch.visibility = if (bool) View.GONE else View.VISIBLE
    }

    private fun searchMovie(keyword: String) {
        view?.let { context?.hideKeyboard(it) }
        lifecycleScope.launch {
            viewModel.getSearchMovie(keyword).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun showDetailMovie(movie: Movie) {
        val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(movie.id)
        findNavController().navigate(action)
    }

}