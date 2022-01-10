package me.dzikry.movapp.ui.home.profile

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import me.dzikry.movapp.R
import me.dzikry.movapp.data.models.User
import me.dzikry.movapp.databinding.FragmentProfileBinding
import me.dzikry.movapp.utils.Const

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var token: String
    private lateinit var user: User

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}