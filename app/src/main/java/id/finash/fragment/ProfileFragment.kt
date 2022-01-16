package id.finash.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import id.finash.R
import id.finash.databinding.FragmentProfileBinding
import id.finash.preference.PreferencesManager
import id.finash.util.PrefUtil

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val pref by lazy { PreferencesManager(requireContext())}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getAvatar()
    }

    private fun getAvatar(){
        binding.imageAvatar.setImageResource(pref.getInt(PrefUtil.pref_avatar)!!)
        binding.textName.text = pref.getString(PrefUtil.pref_name)
        binding.textUsername.text = pref.getString(PrefUtil.pref_username)
        binding.textDate.text = (pref.getString(PrefUtil.pref_date)!!)
    }

    private fun setupListener(){
        binding.imageAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_avatarFragment)
        }
    }

}