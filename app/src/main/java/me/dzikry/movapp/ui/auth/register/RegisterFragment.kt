package me.dzikry.movapp.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.databinding.FragmentRegisterBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.saveToken

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        Tools.setStatusBarTransparent(requireActivity())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            termPrivacy.movementMethod = LinkMovementMethod.getInstance()

            signin.setOnClickListener {
                findNavController().popBackStack()
            }

            register.setOnClickListener {
                val name = name.text.toString().trim()
                val email = email.text.toString().trim()
                val username = username.text.toString().trim()
                val password = password.text.toString().trim()
                val phone = phone.text.toString().trim()

                if (name.isNotEmpty()
                    && email.isNotEmpty()
                    && username.isNotEmpty()
                    && password.isNotEmpty()
                    && phone.isNotEmpty()
                ) {
                    postRegister(name, email, username, password, phone)
                } else {
                    Toast.makeText(context, "Field cannot empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun postRegister(
        name: String,
        email: String,
        username: String,
        password: String,
        phone: String
    ) {
        viewModel.register(name, email, username, password, phone)
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
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    isLoading(true)
                }
            }
        }
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
                progressBarRegister.visibility = View.VISIBLE
                register.visibility = View.INVISIBLE
            } else {
                progressBarRegister.visibility = View.INVISIBLE
                register.visibility = View.VISIBLE
            }
        }
    }

}