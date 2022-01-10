package me.dzikry.movapp.ui.auth.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.data.networks.AuthAPIs
import me.dzikry.movapp.data.repositories.AuthRepository
import me.dzikry.movapp.databinding.FragmentLoginBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.AuthViewModelFactory
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import java.io.Serializable

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val api = AuthAPIs()
        val repo = AuthRepository(api)
        val factory = AuthViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
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
                    Toast.makeText(context, "Field cannot empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun postLogin(email: String, password: String) {
        viewModel.login(email, password)
        viewModel.user.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    isLoading(false)
                    response.data?.let { user ->
                        viewModel.token.observe(viewLifecycleOwner, { token ->
                            activity?.let { Tools.saveToken(it, token) }
                            gotoHome(token, user)
                        })
                    }
                }

                is Resource.Error -> {
                    isLoading(false)
                    response.message?.let { error ->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    isLoading(true)
                }
            }
        })
    }

    private fun gotoHome(token: String?, user: User) {
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