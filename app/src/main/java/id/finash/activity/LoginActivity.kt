package id.finash.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.finash.R
import id.finash.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener(){
        binding.textRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
    }
}