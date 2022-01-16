package id.finash.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.finash.databinding.ActivityRegisterBinding
import id.finash.model.User

class RegisterActivity : BaseActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater)}
    private val db by lazy { Firebase.firestore}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener(){
        binding.textLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        binding.buttonRegister.setOnClickListener{
            if (isRequired()) checkUsername()
            else Toast.makeText(applicationContext, "Data cannot be empty", Toast.LENGTH_SHORT).show()
        }

    }

    private fun progress(progress: Boolean){
        binding.textAlert.visibility = View.GONE
        when(progress){
            true -> {
                binding.buttonRegister.text = "Loading.."
                binding.buttonRegister.isEnabled = false
            }
            false -> {
                binding.buttonRegister.text = "SIGN UP"
                binding.buttonRegister.isEnabled = true
            }
        }
    }

    private fun isRequired(): Boolean {
        return (
                binding.editName.text.isNotEmpty() &&
                binding.editUsername.text.isNotEmpty() &&
                binding.editPassword.text.isNotEmpty()
                )
    }

    private fun checkUsername(){
        progress(true)
        db.collection("user")
            .whereEqualTo("username", binding.editUsername.text.toString())
            .get()
            .addOnSuccessListener { result ->
                progress(false)
                if (result.isEmpty) addUser()
                else binding.textAlert.visibility = View.VISIBLE
            }
    }

    private fun addUser(){
        progress(true)
        val user = User(
            name = binding.editName.text.toString(),
            username = binding.editUsername.text.toString(),
            password = binding.editPassword.text.toString(),
            created = Timestamp.now()
        )

        db.collection("user")
            .add( user )
            .addOnSuccessListener {
                progress(false)
                Toast.makeText(applicationContext,"Register Succes", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }
}