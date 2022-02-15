package me.dzikry.movapp.ui.home.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import me.dzikry.movapp.data.models.Account
import me.dzikry.movapp.databinding.FragmentProfileBinding
import me.dzikry.movapp.ui.auth.AuthViewModel
import me.dzikry.movapp.ui.auth.MainActivity
import me.dzikry.movapp.ui.home.HomeActivity
import me.dzikry.movapp.utils.Const
import me.dzikry.movapp.utils.Resource
import me.dzikry.movapp.utils.Tools.Companion.revokeSessionID

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sessionID: String
    private lateinit var mAccount: Account

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
        binding.apply {
            val bundle: Bundle? = activity?.intent?.extras
            bundle?.let {
                it.apply {
                    sessionID = getString(Const.SESSION_ID)!!
                    val jsonAccount = getString(Const.ACCOUNT)
                    val gson = Gson()
                    mAccount = gson.fromJson(jsonAccount, Account::class.java)

                    account = mAccount
                }
            }
            logout.setOnClickListener {
                actionLogout()
            }
        }
    }

    private fun actionLogout() {
        authViewModel.sessionLogout(sessionID)
        authViewModel.sessionLogout.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    isLoading(false)
                    activity?.revokeSessionID()
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
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

    private fun isLoading(value: Boolean) {
        binding.apply {
            if (value) {
                loading.visibility = View.VISIBLE
                logout.visibility = View.GONE
            } else {
                loading.visibility = View.GONE
                logout.visibility = View.VISIBLE
            }
        }
    }

}