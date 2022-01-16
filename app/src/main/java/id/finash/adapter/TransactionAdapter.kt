package id.finash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.finash.R
import id.finash.databinding.AdapterTransactionBinding
import id.finash.model.Transaction

class TransactionAdapter(
    var transaction: ArrayList<Transaction>,
    var listener: AdapterListener?
    ): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.ViewHolder {
        return ViewHolder(
            AdapterTransactionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        val transaction = transaction[position]

        if (transaction.type.toUpperCase() == "IN") holder.binding.imageType.setImageResource(R.drawable.ic_income)
        else holder.binding.imageType.setImageResource(R.drawable.ic_outcome)

        holder.binding.textNote.text = transaction.note
        holder.binding.textCategory.text = transaction.category
        holder.binding.textAmount.text = transaction.amount.toString()
        holder.binding.textDate.text = transaction.created.toString()

        holder.binding.container.setOnClickListener {
            listener?.onClick(transaction)
        }
    }

    override fun getItemCount() = transaction.size

    class ViewHolder(val binding: AdapterTransactionBinding): RecyclerView.ViewHolder(binding.root)

    interface AdapterListener {
        fun onClick(transaction: Transaction)
    }

}