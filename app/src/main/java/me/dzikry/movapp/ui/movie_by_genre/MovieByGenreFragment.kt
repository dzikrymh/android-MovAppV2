package me.dzikry.movapp.ui.movie_by_genre

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import me.dzikry.movapp.databinding.FragmentMovieByGenreBinding

class MovieByGenreFragment : Fragment() {

    companion object {
        fun newInstance() = MovieByGenreFragment()
    }

    private lateinit var viewModel: MovieByGenreViewModel
    private lateinit var binding: FragmentMovieByGenreBinding
    private val args: MovieByGenreFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieByGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MovieByGenreViewModel::class.java]

        Toast.makeText(context, args.genreId.toString(), Toast.LENGTH_SHORT).show()
    }

}