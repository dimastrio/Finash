package id.finash.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.finash.adapter.TransactionAdapter
import id.finash.model.Category
import id.finash.databinding.ActivityHomeBinding
import id.finash.databinding.HomeAvatarBinding
import id.finash.databinding.HomeDashboardBinding
import id.finash.model.Transaction
import id.finash.preference.PreferencesManager
import id.finash.util.PrefUtil
import id.finash.util.amountFormat

class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater)}
    private lateinit var bindingAvatar: HomeAvatarBinding
    private lateinit var bindingDashboard: HomeDashboardBinding
    private lateinit var transactionAdapter: TransactionAdapter

    private val db by lazy { Firebase.firestore }
    private val pref by lazy { PreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupList()
        setupListener()

    }

    override fun onStart() {
        super.onStart()
        getAvatar()
        getBalance()
        getTransaction()
    }

    private fun getAvatar(){
        bindingAvatar.textAvatar.text = pref.getString(PrefUtil.pref_name)
        bindingAvatar.imageAvatar.setImageResource(pref.getInt(PrefUtil.pref_avatar)!!)
    }

    private fun getBalance(){
        var totalBalance = 0
        var totalIn = 0
        var totalOut = 0
        db.collection("transaction")
            .whereEqualTo("username", pref.getString(PrefUtil.pref_username))
            .get()
            .addOnSuccessListener { result ->
            result.forEach { doc ->
                totalBalance += doc.data["amount"].toString().toInt()
                when(doc.data["type"].toString()){
                    "IN" -> totalIn += doc.data["amount"].toString().toInt()
                    "OUT" -> totalOut += doc.data["amount"].toString().toInt()
                }
            }

                bindingDashboard.textBalance.text = "Rp ${amountFormat(totalBalance)}"
                bindingDashboard.textIn.text = "Rp ${amountFormat(totalIn)}"
                bindingDashboard.textOut.text = "Rp ${amountFormat(totalOut)}"
            }
    }

    private fun getTransaction(){
        binding.progress.visibility = View.VISIBLE
        val transactions: ArrayList<Transaction> = arrayListOf()
        db.collection("transaction")
            .orderBy("created", Query.Direction.DESCENDING)
            .whereEqualTo("username", pref.getString(PrefUtil.pref_username))
            .limit(4)
            .get()
            .addOnSuccessListener { result ->
                binding.progress.visibility = View.GONE
                result.forEach { doc ->
                    transactions.add(
                        Transaction(
                            id = doc.reference.id,
                            username = doc.data["username"].toString(),
                            category = doc.data["category"].toString(),
                            type = doc.data["type"].toString(),
                            amount = doc.data["amount"].toString().toInt(),
                            note = doc.data["note"].toString(),
                            created = doc.data["created"] as Timestamp
                        )
                    )
                }

                transactionAdapter.setData(transactions)
            }
    }


    private fun setupBinding() {
        setContentView(binding.root)
        bindingAvatar = binding.includeAvatar
        bindingDashboard = binding.includeDashboard
    }

    private fun setupListener(){
        bindingAvatar.imageAvatar.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java)
                .putExtra("balance", bindingDashboard.textBalance.text.toString())
            )
        }
        binding.fabCreate.setOnClickListener(){
            startActivity(Intent(this, CreateActivity::class.java))
        }

        binding.textTransaction.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }
    }

    private fun setupList(){
        transactionAdapter = TransactionAdapter(arrayListOf(), null)
        binding.listTransaction.adapter = this.transactionAdapter
    }

    private fun deleteTransaction(id: String){
        db.collection("transaction")
            .document()
            .delete()
            .addOnSuccessListener {
                getTransaction()
                getBalance()
            }
    }
}