package id.finash.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.finash.R
import id.finash.activity.BaseActivity
import id.finash.adapter.CategoryAdapter
import id.finash.databinding.ActivityCreateBinding
import id.finash.model.Category
import id.finash.model.Transaction
import id.finash.model.User
import id.finash.preference.PreferencesManager
import id.finash.util.PrefUtil

class CreateActivity : BaseActivity() {
    final val TAG:String = "CreateActivity"

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater)}
    private val db by lazy { Firebase.firestore }
    private lateinit var categoryAdapter: CategoryAdapter
    private var type: String = "";
    private var category: String = "";
    private val pref by lazy { PreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getCategory()
    }

    private fun setupList(){
        categoryAdapter = CategoryAdapter(this, arrayListOf(),object : CategoryAdapter.AdapterListener{
            override fun onClick(category: Category) {
                this@CreateActivity.category = category.name!!
                Log.e(TAG,this@CreateActivity.category)
            }

        })
        binding.listCategory.adapter = categoryAdapter
    }

    private fun setupListener(){
        binding.buttonIn.setOnClickListener {
            type = "IN"
            setButton(it as MaterialButton)
        }
        binding.buttonOut.setOnClickListener {
            type = "OUT"
            setButton(it as MaterialButton)
        }
        binding.buttonSave.setOnClickListener {
            progress(true)
            val transaction = Transaction(
                id = null,
                username = pref.getString(PrefUtil.pref_username)!!,
                category = category,
                type = type,
                amount = binding.editAmount.text.toString().toInt(),
                note = binding.editNote.text.toString(),
                created = Timestamp.now()
            )

            db.collection("transaction")
                .add( transaction )
                .addOnSuccessListener {
                    progress(false)
                    Toast.makeText(applicationContext,"Transaction Added", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }

    private fun progress(progress: Boolean){
        when(progress){
            true -> {
                binding.buttonSave.text = "Loading.."
                binding.buttonSave.isEnabled = false
            }
            false -> {
                binding.buttonSave.text = "SAVE"
                binding.buttonSave.isEnabled = true
            }
        }
    }

    private fun setButton(buttonSelected: MaterialButton){
        Log.e(TAG,type)
        listOf<MaterialButton>(binding.buttonIn, binding.buttonOut).forEach {
            it.setBackgroundColor(ContextCompat.getColor(this, R.color.blue2))
        }
        buttonSelected.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
    }

    private fun getCategory(){
        val categories: ArrayList<Category> = arrayListOf()
        db.collection("category")
            .get()
            .addOnSuccessListener { result ->
                result.forEach{document ->
                    categories.add( Category(document.data["name"].toString()))
                }

                Log.e("HomeActivity", "categories $categories")
                categoryAdapter.setData(categories)

            }
    }
}