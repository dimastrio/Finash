package id.finash.activity

import android.content.Intent
import android.os.Bundle
import id.finash.databinding.ActivityHomeBinding
import id.finash.databinding.HomeAvatarBinding
import id.finash.databinding.HomeDashboradBinding

class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater)}
    private lateinit var bindingAvatar: HomeAvatarBinding
    private lateinit var bindingDashboard: HomeDashboradBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListener()

    }

    private fun setupBinding() {
        setContentView(binding.root)
        bindingAvatar = binding.includeAvatar
        bindingDashboard = binding.includeDashboard
    }

    private fun setupListener(){
        bindingAvatar.imageAvatar.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.fabCreate.setOnClickListener(){
            startActivity(Intent(this, CreateActivity::class.java))
        }

        binding.textTransaction.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }
    }
}