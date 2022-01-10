package me.dzikry.movapp.ui.auth.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import me.dzikry.movapp.data.networks.AuthAPIs
import me.dzikry.movapp.data.repositories.AuthRepository
import me.dzikry.movapp.databinding.FragmentMainBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.AuthViewModelFactory
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var args: MainFragmentArgs

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val api = AuthAPIs()
        val repo = AuthRepository(api)
        val factory = AuthViewModelFactory(repo)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        arguments?.let {
            args = MainFragmentArgs.fromBundle(it)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            name.text = args.token

            button.setOnClickListener {
                authViewModel.logout(args.token)
                authViewModel.meta.observe(viewLifecycleOwner, { response ->
                    when (response) {
                        is Resource.Success -> {
                            Tools.saveToken(requireActivity(), "")
                            activity?.finish()
                        }

                        is Resource.Error -> {
                            response.message?.let { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        }

                        is Resource.Loading -> {

                        }
                    }
                })
            }
        }
    }

}