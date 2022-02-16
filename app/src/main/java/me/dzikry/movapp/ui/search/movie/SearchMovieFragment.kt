package me.dzikry.movapp.ui.search.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.FragmentSearchMovieBinding
import me.dzikry.movapp.databinding.ItemMovieBinding
import me.dzikry.movapp.ui.adapter.MoviePagingAdapter
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.PagingLoadStateAdapter
import me.dzikry.movapp.utils.SpacingItemDecoration
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.hideKeyboard
import javax.inject.Inject

@AndroidEntryPoint
class SearchMovieFragment : Fragment(), MoviePagingAdapter.MovieClickListener {

    companion object {
        fun newInstance() = SearchMovieFragment()
    }

    private val viewModel: SearchMovieViewModel by viewModels()
    private lateinit var binding: FragmentSearchMovieBinding

    @Inject lateinit var mAdapter: MoviePagingAdapter
    private lateinit var moviesLayoutMgr: StaggeredGridLayoutManager

    private val args: SearchMovieFragmentArgs by navArgs()

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
                        itemCount < 1) {
                        binding.movieNotFound.visibility = View.VISIBLE
                    }
                }
                movieClickListener = this@SearchMovieFragment
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
            viewModel.getSearchMovie(keyword)
        }
        lifecycleScope.launch {
            viewModel.movieFlow.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun showDetailMovie(movie: Movie) {
        val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(movie.id, args.accountId, args.sessionId)
        findNavController().navigate(action)
    }

    override fun onMovieClicked(binding: ItemMovieBinding, movie: Movie) {
        showDetailMovie(movie)
    }

}