package me.dzikry.movapp.ui.movie_by_genre

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.databinding.FragmentMovieByGenreBinding
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.ui.movie_by_genre.adapter.MovieByGenreAdapter
import me.dzikry.movapp.utils.PagingLoadStateAdapter
import me.dzikry.movapp.utils.SpacingItemDecoration
import me.dzikry.movapp.utils.Tools

class MovieByGenreFragment : Fragment() {

    companion object {
        fun newInstance() = MovieByGenreFragment()
    }

    private lateinit var viewModel: MovieByGenreViewModel
    private lateinit var binding: FragmentMovieByGenreBinding

    private lateinit var mAdapter: MovieByGenreAdapter
    private lateinit var moviesLayoutMgr: StaggeredGridLayoutManager

    private val args: MovieByGenreFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val api = MovieAPIs()
        val factory = MovieByGenreViewModelFactory(api = api)
        viewModel = ViewModelProvider(this, factory)[MovieByGenreViewModel::class.java]
    }

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
        mAdapter = MovieByGenreAdapter { movie -> showDetailMovie(movie) }
        binding.apply {
            discoverMovies.layoutManager = StaggeredGridLayoutManager(Tools.getGridSpanCountMovie(requireActivity()), StaggeredGridLayoutManager.VERTICAL)
            val decoration = SpacingItemDecoration(Tools.getGridSpanCountMovie(requireActivity()), Tools.dpToPx(requireContext(), 2), false)
            discoverMovies.addItemDecoration(decoration)
            mAdapter.apply {
                discoverMovies.adapter = withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(this),
                    footer = PagingLoadStateAdapter(this)
                )
            }
        }

        lifecycleScope.launch {
            viewModel.getDiscoverMovieByGenre(args.genreId.toString()).collectLatest {
                mAdapter.submitData(it)
            }
        }

    }

    private fun showDetailMovie(movie: Movie) {
        val action = MovieByGenreFragmentDirections.actionMovieByGenreFragmentToMovieDetailFragment(movie.id)
        findNavController().navigate(action)
    }

}