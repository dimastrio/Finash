package id.finash.activity

import android.os.Bundle
import id.finash.activity.BaseActivity
import id.finash.databinding.ActivityCreateBinding

class CreateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}