package me.dzikry.movapp.ui.home.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.databinding.FragmentProfileBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.MainActivity
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools.Companion.restoreToken
import me.dzikry.movapp.utils.Tools.Companion.revokeToken

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var token: String
    private lateinit var mUser: User

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
            val bundle: Bundle? = activity?.intent?.extras
            bundle?.let {
                it.apply {
                    token = getString(Const.TOKEN)!!
                    val jsonUser = getString(Const.USER)
                    val gson = Gson()
                    mUser = gson.fromJson(jsonUser, User::class.java)

                    user = mUser
                }
            }
            logout.setOnClickListener {
                actionLogout()
            }
        }
    }

    private fun actionLogout() {
        authViewModel.logout(token)
        authViewModel.meta.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    activity?.revokeToken()

                    val token = activity?.restoreToken()
                    Log.i("Profile_Screen", "token=" + token.toString())

                    startActivity(Intent(context, MainActivity::class.java))
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
        }
    }

}