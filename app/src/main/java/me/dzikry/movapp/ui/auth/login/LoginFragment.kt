package me.dzikry.movapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.databinding.FragmentLoginBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.MainActivity
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.saveToken

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        Tools.setStatusBarTransparent(requireActivity())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            signup.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }

            login.setOnClickListener {
                val email = email.text.toString().trim()
                val password = password.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    postLogin(email, password)
                } else {
                    MainActivity.toastTop(
                        "Field cannot empty",
                        resources.getColor(R.color.white), resources.getColor(
                            R.color.error),
                    )
                }
            }
        }

    }

    private fun postLogin(email: String, password: String) {
        viewModel.login(email, password)
        viewModel.user.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoading(false)
                    response.data?.let { user ->
                        viewModel.token.observe(viewLifecycleOwner) { token ->
                            activity?.saveToken(token = token)
                            gotoHome(token, user)
                        }
                    }
                }

                is Resource.Error -> {
                    isLoading(false)
                    response.message?.let { error ->
                        MainActivity.toastTop(
                            error,
                            resources.getColor(R.color.white), resources.getColor(
                            R.color.error),
                        )
                    }
                }

                is Resource.Loading -> {
                    isLoading(true)
                }
            }
        }
    }

    private fun gotoHome(token: String, user: User) {
        val gson = Gson()
        val jsonUser: String = gson.toJson(user)

        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(Const.TOKEN, token)
        intent.putExtra(Const.USER, jsonUser)
        startActivity(intent)
        activity?.finish()
    }

    private fun isLoading(it: Boolean) {
        binding.apply {
            if (it) {
                progressBarLogin.visibility = View.VISIBLE
                login.visibility = View.INVISIBLE
            } else {
                progressBarLogin.visibility = View.INVISIBLE
                login.visibility = View.VISIBLE
            }
        }
    }
}