package me.dzikry.movapp.ui.home.movie

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import me.dzikry.movapp.data.models.Genre
import me.dzikry.movapp.data.models.Movie
import me.dzikry.movapp.data.networks.MovieAPIs
import me.dzikry.movapp.data.repositories.MovieRepository
import me.dzikry.movapp.databinding.FragmentMovieBinding
import me.dzikry.movapp.ui.home.movie.adapter.GenreAdapter
import me.dzikry.movapp.ui.home.movie.adapter.MovieAdapter
import me.dzikry.movapp.ui.home.movie.adapter.MovieMotionAdapter
import me.dzikry.movapp.utils.Resource

class MovieFragment : Fragment() {

    companion object {
        fun newInstance() = MovieFragment()
    }

    private lateinit var viewModel: MovieViewModel
    private lateinit var binding: FragmentMovieBinding

    private lateinit var popularAdapter: MovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var upcomingAdapter: MovieMotionAdapter
    private lateinit var genreAdapter: GenreAdapter

    private var popularPage = 1
    private var upcomingPage = 1
    private var topRatedPage = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val api = MovieAPIs()
        val repo = MovieRepository(api)
        val factory = MovieViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        viewModel.getUpcoming(upcomingPage)
        viewModel.getGenre()
        viewModel.getPopular(popularPage)
        viewModel.getTopRated(topRatedPage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.imageSearch.setOnClickListener {
            val action = MovieFragmentDirections.actionMovieFragmentToSearchMovieFragment()
            findNavController().navigate(action)
        }

        viewModel.upcoming.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingUpcoming(false)
                    response.data?.let {
                        upcomingAdapter = MovieMotionAdapter { movie -> showMovieDetail(movie) }
                        binding.motionUpcoming.apply {
                            adapter = upcomingAdapter
                        }
                        upcomingAdapter.differ.submitList(it)
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
        })

        viewModel.genre.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingGenre(false)
                    response.data?.let {
                        genreAdapter = GenreAdapter { genre -> showMovieByGenre(genre) }
                        binding.genreMovies.apply {
                            adapter = genreAdapter
                        }
                        genreAdapter.differ.submitList(it)
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
        })

        viewModel.popular.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingPopular(false)
                    response.data?.let {
                        popularAdapter = MovieAdapter { movie -> showMovieDetail(movie) }
                        binding.popularMovies.apply {
                            adapter = popularAdapter
                        }
                        popularAdapter.differ.submitList(it)
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
        })

        viewModel.topRated.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    isLoadingTopRated(false)
                    response.data?.let {
                        topRatedAdapter = MovieAdapter { movie -> showMovieDetail(movie) }
                        binding.topRatedMovies.apply {
                            adapter = topRatedAdapter
                        }
                        topRatedAdapter.differ.submitList(it)
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
        })
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