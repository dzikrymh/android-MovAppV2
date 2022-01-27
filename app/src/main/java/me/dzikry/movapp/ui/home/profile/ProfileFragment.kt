package me.dzikry.movapp.ui.home.profile

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.data.networks.AuthAPIs
import me.dzikry.movapp.data.repositories.AuthRepository
import me.dzikry.movapp.databinding.FragmentProfileBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.AuthViewModelFactory
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var token: String
    private lateinit var user: User

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val api = AuthAPIs()
        val repo = AuthRepository(api)
        val factory = AuthViewModelFactory(repo)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        val bundle: Bundle? = activity?.intent?.extras
        bundle?.let { bundle ->
            bundle.apply {
                token = getString(Const.TOKEN)!!
                val jsonUser = getString(Const.USER)

                val gson = Gson()
                user = gson.fromJson(jsonUser, User::class.java)

                Toast.makeText(context, "${user.name} - $token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        HomeActivity.animate(false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        binding.apply {
            Glide.with(requireContext())
                .load(user.profile_photo_url)
                .transform(CenterCrop())
                .into(image)

            name.text = user.name
            username.text = "@${user.username}"

            logout.setOnClickListener {
                actionLogout()
            }
        }
    }

    private fun actionLogout() {
        authViewModel.logout(token)
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