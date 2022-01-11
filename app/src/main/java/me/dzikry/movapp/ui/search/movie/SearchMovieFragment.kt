package me.dzikry.movapp.ui.search.movie

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.dzikry.movapp.R

class SearchMovieFragment : Fragment() {

    companion object {
        fun newInstance() = SearchMovieFragment()
    }

    private lateinit var viewModel: SearchMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchMovieViewModel::class.java)
        // TODO: Use the ViewModel
    }

}