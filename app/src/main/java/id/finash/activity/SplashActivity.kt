package id.finash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.finash.R
import id.finash.databinding.ActivityLoginBinding
import id.finash.preference.PreferencesManager

class SplashActivity : BaseActivity() {


    private val pref by lazy { PreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.myLooper()!!).postDelayed({
            if (pref.getInt("pref_is_login") == 0){
                startActivity(Intent(this, LoginActivity::class.java))
            }else startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 2000)
    }
}