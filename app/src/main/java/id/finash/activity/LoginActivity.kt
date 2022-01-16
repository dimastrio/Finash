package id.finash.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.finash.R
import id.finash.databinding.ActivityLoginBinding
import id.finash.model.User

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}
    private val db by lazy { Firebase.firestore}

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

        binding.buttonLogin.setOnClickListener {
            if (isRequired()) login()
            else Toast.makeText(applicationContext, "Data cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(){
        progress(true)
        db.collection("user")
            .whereEqualTo("username", binding.editUsername.text.toString())
            .whereEqualTo("password", binding.editPassword.text.toString())
            .get()
            .addOnSuccessListener { result ->
                progress(false)
                if (result.isEmpty) binding.textAlert.visibility = View.VISIBLE
                else {
                    result.forEach{ document ->
                        saveSession(
                            User (
                                name = document.data["name"].toString(),
                                username = document.data["username"].toString(),
                                password = document.data["password"].toString(),
                                created = document.data["created"] as Timestamp
                            )
                        )
                    }
                    Toast.makeText(applicationContext, "Login Succes", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun isRequired(): Boolean {
        return (
                    binding.editUsername.text.isNotEmpty() &&
                    binding.editUsername.text.isNotEmpty()
                )
    }

    private fun progress(progress: Boolean){
        binding.textAlert.visibility = View.GONE
        when(progress){
            true -> {
                binding.buttonLogin.text = "Loading.."
                binding.buttonLogin.isEnabled = false
            }
            false -> {
                binding.buttonLogin.text = "SIGN IN"
                binding.buttonLogin.isEnabled = true
            }
        }
    }

    private fun saveSession(user: User){
        Log.e("LoginActivity", user.toString())
    }
}