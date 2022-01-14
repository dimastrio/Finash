package id.finash.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import id.finash.databinding.ActivityTransactionBinding
import id.finash.fragment.DateFragment

class TransactionActivity : BaseActivity() {

    private val binding by lazy { ActivityTransactionBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {
        binding.imageDate.setOnClickListener{
            DateFragment(object : DateFragment.DateListener{
                override fun onSucces(dateStart: String, dateEnd: String) {
                    Log.e("TransactionActivity","$dateStart $dateEnd")
                }
            }).apply {
                show(supportFragmentManager, "dateFragment")
            }
        }
    }
}