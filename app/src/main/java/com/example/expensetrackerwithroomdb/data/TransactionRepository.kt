package com.example.expensetrackerwithroomdb.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: LiveData<List<TransactionEntity>> = transactionDao.getAllTransactions()

    // Perform insert operation on a background thread
    suspend fun insert(transaction: TransactionEntity) {
        withContext(Dispatchers.IO) {
            transactionDao.insert(transaction)
        }
    }
}
