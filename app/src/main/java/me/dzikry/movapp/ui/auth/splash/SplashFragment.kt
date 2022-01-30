package me.dzikry.movapp.ui.auth.splash

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kotlinx.serialization.descriptors.StructureKind
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.data.networks.AuthAPIs
import me.dzikry.movapp.data.repositories.AuthRepository
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.AuthViewModelFactory
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.restoreToken
import java.io.Serializable

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: AuthViewModel
    private var token: String? = null

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
        Tools.setStatusBarTransparent(requireActivity())

        activity?.restoreToken()?.let {
            token = it
            Log.i("Splash_Screen","token=" + it)
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (token.isNullOrEmpty()) {
            Handler().postDelayed({
                gotoLogin()
            }, 3000)
        } else {
            viewModel.getUser(token = token!!)
            viewModel.user.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { user ->
                            gotoHome(token!!, user)
                        }
                    }

                    is Resource.Error -> {
                        Handler().postDelayed({
                            gotoLogin()
                        }, 3000)

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

    private fun gotoLogin() {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        findNavController().navigate(action)
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

}