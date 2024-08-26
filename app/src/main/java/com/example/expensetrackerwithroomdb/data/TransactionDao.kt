package com.example.expensetrackerwithroomdb.data
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY DATE desc , TIME desc")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Insert
    fun insert(transaction: TransactionEntity)

    @Query("DELETE FROM transactions")
    fun clearAllTransactions()
}
