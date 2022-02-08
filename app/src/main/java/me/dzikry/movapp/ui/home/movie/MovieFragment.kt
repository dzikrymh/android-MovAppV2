package me.dzikry.movapp.ui.home.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.databinding.FragmentMovieBinding
import me.dzikry.movapp.databinding.ItemGenreMovieBinding
import me.dzikry.movapp.databinding.ItemMotionMovieBinding
import me.dzikry.movapp.databinding.ItemMovieBinding
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.ui.adapter.GenreAdapter
import me.dzikry.movapp.ui.adapter.MovieAdapter
import me.dzikry.movapp.ui.adapter.MovieMotionAdapter
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment(), MovieAdapter.MovieClickListener,
    MovieMotionAdapter.MovieMotionClickListener, GenreAdapter.GenreClickListener {

    companion object {
        fun newInstance() = MovieFragment()
    }

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var binding: FragmentMovieBinding

    @Inject lateinit var popularAdapter: MovieAdapter
    @Inject lateinit var topRatedAdapter: MovieAdapter
    @Inject lateinit var upcomingAdapter: MovieMotionAdapter
    @Inject lateinit var genreAdapter: GenreAdapter

    private var popularPage = 1
    private var upcomingPage = 1
    private var topRatedPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(layoutInflater, container, false)

        Tools.setStatusBarTransparent(requireActivity())
        Tools.setMargins(
            binding.imageLogo,
            left = Tools.dpToPx(requireContext(), 16),
            top = Tools.dpToPx(requireContext(), 16) + Tools.getStatusBarHeight(requireContext()),
            right = 0,
            bottom = 0
        )
        Tools.setMargins(
            binding.imageSearch,
            left = 0,
            top = Tools.dpToPx(requireContext(), 16) + Tools.getStatusBarHeight(requireContext()),
            right = Tools.dpToPx(requireContext(), 16),
            bottom = 0
        )
        HomeActivity.animate(false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.imageSearch.setOnClickListener {
            val action = MovieFragmentDirections.actionMovieFragmentToSearchMovieFragment()
            findNavController().navigate(action)
        }

        viewModel.getUpcoming(upcomingPage)
        viewModel.upcoming.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingUpcoming(false)
                    response.data?.let {
                        with(upcomingAdapter) {
                            binding.motionUpcoming.apply {
                                adapter = this@with
                            }
                            differ.submitList(it)
                            movieMotionClickListener = this@MovieFragment
                        }
                    }
                }
                is Resource.Error -> {
                    isLoadingUpcoming(false)
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    isLoadingUpcoming(true)
                }
            }
        }

        viewModel.getGenre()
        viewModel.genre.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingGenre(false)
                    response.data?.let {
                        with(genreAdapter) {
                            binding.genreMovies.apply {
                                adapter = this@with
                            }
                            differ.submitList(it)
                            genreClickListener = this@MovieFragment
                        }
                    }
                }
                is Resource.Error -> {
                    isLoadingGenre(false)
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    isLoadingGenre(true)
                }
            }
        }

        viewModel.getPopular(popularPage)
        viewModel.popular.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingPopular(false)
                    response.data?.let {
                        with(popularAdapter) {
                            binding.popularMovies.apply {
                                adapter = this@with
                            }
                            differ.submitList(it)
                            movieClickListener = this@MovieFragment
                        }
                    }
                }
                is Resource.Error -> {
                    isLoadingPopular(true)
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    isLoadingPopular(true)
                }
            }
        }

        viewModel.getTopRated(topRatedPage)
        viewModel.topRated.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingTopRated(false)
                    response.data?.let {
                        with(topRatedAdapter) {
                            binding.topRatedMovies.apply {
                                adapter = this@with
                            }
                            differ.submitList(it)
                            movieClickListener = this@MovieFragment
                        }
                    }
                }
                is Resource.Error -> {
                    isLoadingTopRated(true)
                    response.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    isLoadingTopRated(true)
                }
            }
        }
    }

    override fun onMovieClicked(binding: ItemMovieBinding, movie: Movie) {
        showMovieDetail(movie)
    }

    override fun onMovieMotionClicked(binding: ItemMotionMovieBinding, movie: Movie) {
        showMovieDetail(movie)
    }

    override fun onGenreClicked(binding: ItemGenreMovieBinding, genre: Genre) {
        showMovieByGenre(genre)
    }

    private fun showMovieByGenre(genre: Genre) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieByGenreFragment(genre.id)
        findNavController().navigate(action)
    }

    private fun showMovieDetail(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movie.id)
        findNavController().navigate(action)
    }

    private fun isLoadingPopular(bool: Boolean) {
        binding.apply {
            popularMovies.visibility = if (bool) View.GONE else View.VISIBLE
            shimmerPopular.visibility = if (bool) View.VISIBLE else View.GONE
        }
    }

    private fun isLoadingUpcoming(bool: Boolean) {
        binding.apply {
            motionUpcoming.visibility = if (bool) View.GONE else View.VISIBLE
            shimmerUpcoming.visibility = if (bool) View.VISIBLE else View.GONE
        }
    }

    private fun isLoadingTopRated(bool: Boolean) {
        binding.apply {
            topRatedMovies.visibility = if (bool) View.GONE else View.VISIBLE
            shimmerTopRated.visibility = if (bool) View.VISIBLE else View.GONE
        }
    }

    private fun isLoadingGenre(bool: Boolean) {
        binding.apply {
            genreMovies.visibility = if (bool) View.GONE else View.VISIBLE
            shimmerGenre.visibility = if (bool) View.VISIBLE else View.GONE
        }
    }

}