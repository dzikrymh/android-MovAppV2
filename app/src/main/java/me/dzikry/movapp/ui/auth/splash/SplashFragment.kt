package me.dzikry.movapp.ui.auth.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.MainActivity
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools
import me.dzikry.movapp.utils.Tools.Companion.restoreSessionID

@AndroidEntryPoint
class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Tools.setStatusBarTransparent(requireActivity())
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.restoreSessionID()?.let {
            viewModel.accountDetail(it)
            viewModel.account.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { account ->
                            gotoHome(it, account)
                        }
                    }
                    is Resource.Error -> {
                        gotoLogin()
                        response.message?.let { error ->
                            MainActivity.toastTop(error, resources.getColor(R.color.white), resources.getColor(R.color.error))
                        }
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        } ?: run {
            Handler().postDelayed({
                gotoLogin()
            }, 3000)
        }
    }

    private fun gotoLogin() {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        findNavController().navigate(action)
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

}