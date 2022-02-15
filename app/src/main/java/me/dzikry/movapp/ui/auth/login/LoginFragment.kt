package me.dzikry.movapp.ui.auth.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.databinding.FragmentLoginBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.MainActivity
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.saveSessionID

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
                val url = "https://www.themoviedb.org/"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            login.setOnClickListener {
                val user = username.text.toString().trim()
                val pass = password.text.toString().trim()

                if (user.isNotEmpty() && pass.isNotEmpty()) {
                    postLogin(user, pass)
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

    private fun postLogin(username: String, password: String) {
        try {
            viewModel.requestToken()
            viewModel.reqToken.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.request_token?.let {
                            viewModel.validateLogin(
                                username = username,
                                password = password,
                                request_token = it
                            )
                        } ?: run {
                            throw Exception("Something is wrong")
                        }
                    }

                    is Resource.Error -> {
                        throw Exception(response.message)
                    }

                    is Resource.Loading -> {
                        isLoading(true)
                    }
                }
            }
            viewModel.validateLogin.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.request_token?.let {
                            viewModel.requestSessionID(it)
                        } ?: run {
                            throw Exception("Something is wrong")
                        }
                    }

                    is Resource.Error -> {
                        throw Exception(response.message)
                    }

                    is Resource.Loading -> {
                        isLoading(true)
                    }
                }
            }
            viewModel.sessionIDState.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.session_id?.let {
                            viewModel.sessionID.observe(viewLifecycleOwner) { session_id ->
                                activity?.saveSessionID(session_id)
                                viewModel.accountDetail(session_id)
                            }
                        } ?: run {
                            throw Exception("Something is wrong")
                        }
                    }

                    is Resource.Error -> {
                        throw Exception(response.message)
                    }

                    is Resource.Loading -> {
                        isLoading(true)
                    }
                }
            }
            viewModel.account.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        isLoading(false)
                        response.data?.let { account ->
                            viewModel.sessionID.observe(viewLifecycleOwner) { sessionID ->
                                gotoHome(sessionID, account)
                            }
                        } ?: run {
                            throw Exception("Something is wrong")
                        }
                    }

                    is Resource.Error -> {
                        throw Exception(response.message)
                    }

                    is Resource.Loading -> {
                        isLoading(true)
                    }
                }
            }
        } catch (e: Exception) {
            isLoading(false)

            MainActivity.toastTop(
                e.message ?: run {"Something is wrong"},
                resources.getColor(R.color.white), resources.getColor(
                    R.color.error),
            )
        }
    }

    private fun gotoHome(session_id: String, account: Account) {
        val gson = Gson()
        val jsonAccount: String = gson.toJson(account)

        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(Const.ACCOUNT, jsonAccount)
        intent.putExtra(Const.SESSION_ID, session_id)
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