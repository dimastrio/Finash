package id.finash.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.finash.databinding.ActivityCreateBinding

class UpdateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener(){
        binding.buttonSave.setText("Simpan Perubahan")
        binding.buttonSave.setOnClickListener{

        }
    }
}