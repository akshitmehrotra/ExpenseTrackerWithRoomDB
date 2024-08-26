package com.example.expensetrackerwithroomdb
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetrackerwithroomdb.data.TransactionAdapter
import com.example.expensetrackerwithroomdb.databinding.ActivityExpenseHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseHistoryBinding
    private lateinit var transactionAdapter: TransactionAdapter

    // Use ViewModelProvider as per your preference
    private val budgetViewModel: BudgetViewModel by lazy {
        ViewModelProvider(this).get(BudgetViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        transactionAdapter = TransactionAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = transactionAdapter

        // Observe the LiveData from ViewModel
        budgetViewModel.allTransactions.observe(this) { transactions ->
            transactions?.let {
                transactionAdapter.updateTransactions(it)
            }
        }

        // Set up the Clear All Data button
        binding.clearAllButton.setOnClickListener {
            clearAllData()
        }
    }

    private fun clearAllData() {
        GlobalScope.launch(Dispatchers.IO) {
            budgetViewModel.clearAllTransactions()
            withContext(Dispatchers.Main) {
                // Clear the adapter's data and refresh the RecyclerView
                transactionAdapter.updateTransactions(emptyList())
            }
        }
    }
}
