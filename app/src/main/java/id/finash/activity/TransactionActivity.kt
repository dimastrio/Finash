package id.finash.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.finash.adapter.TransactionAdapter
import id.finash.databinding.ActivityTransactionBinding
import id.finash.fragment.DateFragment
import id.finash.model.Transaction
import id.finash.preference.PreferencesManager
import id.finash.util.PrefUtil
import id.finash.util.stringToTimestamp

class TransactionActivity : BaseActivity() {

    private val binding by lazy { ActivityTransactionBinding.inflate(layoutInflater)}
    private val db by lazy { Firebase.firestore }
    private val pref by lazy { PreferencesManager(this) }
    private lateinit var transactionAdapter: TransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getTransaction()
    }

    private fun setupList(){
        transactionAdapter = TransactionAdapter(arrayListOf(), object : TransactionAdapter.AdapterListener{
            override fun onClick(transaction: Transaction) {
                startActivity(
                    Intent(this@TransactionActivity, UpdateActivity::class.java)
                    .putExtra("id", transaction.id)
                )
            }

            override fun onLongClick(transaction: Transaction) {
                val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this@TransactionActivity)
                alertDialog.apply {
                    setTitle("Delete")
                    setMessage("Delete ${transaction.note} from transaction history?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Delete") { dialog, _ ->
                        deleteTransaction(transaction.id!!)
                        dialog.dismiss()
                    }
                }
                alertDialog.show()
            }

        })
        binding.listTransaction.adapter = this.transactionAdapter
    }

    private fun setupListener() {
        binding.swipe.setOnRefreshListener {
            binding.textTransaction.text = "Showing the last transaction"
            getTransaction()
        }
        binding.imageDate.setOnClickListener{
            DateFragment(object : DateFragment.DateListener{
                override fun onSucces(dateStart: String, dateEnd: String) {
                    Log.e("TransactionActivity","$dateStart $dateEnd")
                    binding.textTransaction.text = "$dateStart $dateEnd"
                    db.collection("transaction")
                        .orderBy("created", Query.Direction.DESCENDING)
                        .whereEqualTo("username", pref.getString(PrefUtil.pref_username))
                        .whereGreaterThanOrEqualTo("created", stringToTimestamp("$dateStart 00:00")!!)
                        .whereLessThanOrEqualTo("created", stringToTimestamp("$dateEnd 23:59")!!)
                        .get()
                        .addOnSuccessListener { result ->
                            binding.swipe.isRefreshing = false
                            setTransaction(result)
                        }
                }
            }).apply {
                show(supportFragmentManager, "dateFragment")
            }
        }
    }

    private fun getTransaction(){
        binding.swipe.isRefreshing = true
        db.collection("transaction")
            .orderBy("created", Query.Direction.DESCENDING)
            .whereEqualTo("username", pref.getString(PrefUtil.pref_username))
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                binding.swipe.isRefreshing = false
                setTransaction(result)

            }
    }

    private fun setTransaction(result: QuerySnapshot){
        val transactions: ArrayList<Transaction> = arrayListOf()
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

    private fun deleteTransaction(id: String){
        db.collection("transaction").document(id)
            .delete()
            .addOnSuccessListener { getTransaction()
            }
    }
}