package com.example.expensetrackerwithroomdb

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerwithroomdb.data.AppDatabase
import com.example.expensetrackerwithroomdb.data.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao = AppDatabase.getDatabase(application).transactionDao()
    val allTransactions: LiveData<List<TransactionEntity>> = transactionDao.getAllTransactions()

    // SharedPreferences for storing balance and total budget
    private val sharedPreferences = application.getSharedPreferences("budget_prefs", Context.MODE_PRIVATE)

    private val _totalBudget = MutableLiveData<Double>(sharedPreferences.getString("total_budget", "0.0")?.toDouble() ?: 0.0)
    val totalBudget: LiveData<Double> get() = _totalBudget

    private val _currentBalance = MutableLiveData<Double>(sharedPreferences.getString("current_balance", "0.0")?.toDouble() ?: 0.0)
    val currentBalance: LiveData<Double> get() = _currentBalance

    fun setInitialBudget(amount: Double) {
        if((_currentBalance.value ?: 0.0 )> amount)
        {
            Toast.makeText(getApplication(), "Budget cannot be less that current balance", Toast.LENGTH_SHORT).show()
        }
        else {
            _totalBudget.value = amount
            if (_currentBalance.value == 0.0) {
                _currentBalance.value = amount
            }
            saveToPreferences()
        }
    }

    fun updateBudget(amount: Double) {
        if((_currentBalance.value?: 0.0) + amount < 0)
        {
            Toast.makeText(getApplication(), "Balance cannot be below 0", Toast.LENGTH_SHORT).show()
        }
        else if((_currentBalance.value?: 0.0) + amount > (_totalBudget.value ?: 0.0))
        {
            Toast.makeText(getApplication(), "Balance cannot be greater than total Budget, Change total budget by pressing edit button", Toast.LENGTH_SHORT).show()
        }
        else {
            _currentBalance.value = (_currentBalance.value ?: 0.0) + amount
            saveToPreferences()
            viewModelScope.launch {
                saveTransaction(amount)
            }
        }
    }

    private fun saveTransaction(amount: Double) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val currentTime = timeFormat.format(Date())

        val transactionType = if (amount > 0) "Deposit" else "Withdraw"
        val remainingBalance = _currentBalance.value ?: 0.0

        val transaction = TransactionEntity(
            type = transactionType,
            amount = amount,
            balanceRemaining = remainingBalance,
            date = currentDate,
            time = currentTime
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                transactionDao.insert(transaction)
            }
        }
    }

    fun clearAllTransactions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                transactionDao.clearAllTransactions()
            }
        }
    }

    private fun saveToPreferences() {
        sharedPreferences.edit().apply {
            putString("total_budget", _totalBudget.value.toString())
            putString("current_balance", _currentBalance.value.toString())
            apply()
        }
    }


}
