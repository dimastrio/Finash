package id.finash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.finash.R

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
        }, 2000)
    }
}