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
import id.finash.preference.PreferencesManager
import id.finash.util.timestampToString

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}
    private val db by lazy { Firebase.firestore}
    private val pref by lazy { PreferencesManager(this)}

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
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
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
        pref.put("pref_is_login",1)
        pref.put("pref_name",user.name)
        pref.put("pref_username",user.username)
        pref.put("pref_password",user.password)
        pref.put("pref_date", timestampToString(user.created)!!)
        if (pref.getInt("pref_avatar") == 0) pref.put("pref_avatar",R.drawable.avatar1)
    }
}