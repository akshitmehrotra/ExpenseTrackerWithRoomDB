package com.example.expensetrackerwithroomdb.data

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerwithroomdb.R

class TransactionAdapter(private var transactions: List<TransactionEntity>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDate: TextView = itemView.findViewById(R.id.text_date)
        val textTime: TextView = itemView.findViewById(R.id.text_time)
        val textAmount: TextView = itemView.findViewById(R.id.text_amount)
        val textBalance: TextView = itemView.findViewById(R.id.text_balance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.textDate.text = transaction.date
        holder.textTime.text = transaction.time
        val formattedAmount = formatAmount(transaction.amount, transaction.type)
        holder.textAmount.text = formattedAmount
        holder.textAmount.setTextColor(getAmountColor(transaction.type))
        holder.textBalance.text = "Balance: ${formatAmount(transaction.balanceRemaining, transaction.type)}"
    }

    override fun getItemCount(): Int = transactions.size

    fun updateTransactions(newTransactions: List<TransactionEntity>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }

    private fun formatAmount(amount: Double, type: String): String {
        val formattedAmount = "₹${"%.2f".format(amount)}"
        return when (type) {
            "Deposit" -> "+$formattedAmount"
            "Withdraw" -> "-$formattedAmount"
            else -> formattedAmount
        }
    }

    private fun getAmountColor(type: String): Int {
        return if (type == "Deposit") {
            Color.parseColor("#00FF00") // green
        } else {
            Color.parseColor("#FF0000") // red
        }
    }
}
