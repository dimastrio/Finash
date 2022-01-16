package id.finash.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.finash.model.Category
import id.finash.databinding.ActivityHomeBinding
import id.finash.databinding.HomeAvatarBinding
import id.finash.databinding.HomeDashboradBinding
import id.finash.preference.PreferencesManager
import id.finash.util.PrefUtil

class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater)}
    private lateinit var bindingAvatar: HomeAvatarBinding
    private lateinit var bindingDashboard: HomeDashboradBinding

    private val db by lazy { Firebase.firestore }
    private val pref by lazy { PreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListener()


    }

    override fun onStart() {
        super.onStart()
        testFirestore()
        getAvatar()
    }

    private fun getAvatar(){
        bindingAvatar.textAvatar.text = pref.getString(PrefUtil.pref_name)
        bindingAvatar.imageAvatar.setImageResource(pref.getInt(PrefUtil.pref_avatar)!!)
    }

    private fun testFirestore(){
        val categories: ArrayList<Category> = arrayListOf()
        db.collection("category")
            .get()
            .addOnSuccessListener { result ->
                result.forEach{document ->
                    categories.add( Category(document.data["name"].toString()))
                }

                Log.e("HomeActivity", "categories $categories")

            }
    }

    private fun setupBinding() {
        setContentView(binding.root)
        bindingAvatar = binding.includeAvatar
        bindingDashboard = binding.includeDashboard
    }

    private fun setupListener(){
        bindingAvatar.imageAvatar.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.fabCreate.setOnClickListener(){
            startActivity(Intent(this, CreateActivity::class.java))
        }

        binding.textTransaction.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }
    }
}