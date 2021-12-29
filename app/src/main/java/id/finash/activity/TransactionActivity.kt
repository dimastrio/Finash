package id.finash.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.finash.databinding.ActivityTransactionBinding
import id.finash.fragment.DateFragment

class TransactionActivity : BaseActivity() {

    private val binding by lazy { ActivityTransactionBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setupListener() {
        binding.imageDate.setOnClickListener{

        }
    }
}