package me.dzikry.movapp.ui.movie_by_genre

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.FragmentMovieByGenreBinding
import me.dzikry.movapp.databinding.ItemMovieBinding
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.ui.adapter.MoviePagingAdapter
import me.dzikry.movapp.utils.PagingLoadStateAdapter
import me.dzikry.movapp.utils.SpacingItemDecoration
import me.dzikry.movapp.utils.Tools
import javax.inject.Inject

@AndroidEntryPoint
class MovieByGenreFragment : Fragment(), MoviePagingAdapter.MovieClickListener {

    companion object {
        fun newInstance() = MovieByGenreFragment()
    }

    private val viewModel: MovieByGenreViewModel by viewModels()
    private lateinit var binding: FragmentMovieByGenreBinding

    @Inject lateinit var mAdapter: MoviePagingAdapter
    private lateinit var moviesLayoutMgr: StaggeredGridLayoutManager

    private val args: MovieByGenreFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieByGenreBinding.inflate(inflater, container, false)

        Tools.setStatusBarTransparent(requireActivity())
        Tools.setMargins(
            binding.titleGenre,
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
        with(mAdapter) {
            binding.apply {
                discoverMovies.layoutManager = StaggeredGridLayoutManager(Tools.getGridSpanCountMovie(requireActivity()), StaggeredGridLayoutManager.VERTICAL)
                val decoration = SpacingItemDecoration(Tools.getGridSpanCountMovie(requireActivity()), Tools.dpToPx(requireContext(), 2), false)
                discoverMovies.addItemDecoration(decoration)
                discoverMovies.adapter = withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(this@with),
                    footer = PagingLoadStateAdapter(this@with)
                )
            }
            movieClickListener = this@MovieByGenreFragment
        }

        lifecycleScope.launch {
            viewModel.getDiscoverMovieByGenre(args.genreId.toString())
        }
        lifecycleScope.launch {
            viewModel.movieFlow.collectLatest {
                mAdapter.submitData(it)
            }
        }

    }

    private fun showDetailMovie(movie: Movie) {
        val action = MovieByGenreFragmentDirections.actionMovieByGenreFragmentToMovieDetailFragment(movie.id)
        findNavController().navigate(action)
    }

    override fun onMovieClicked(binding: ItemMovieBinding, movie: Movie) {
        showDetailMovie(movie)
    }

}