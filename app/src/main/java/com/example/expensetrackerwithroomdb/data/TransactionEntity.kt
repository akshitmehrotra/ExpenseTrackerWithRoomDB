package com.example.expensetrackerwithroomdb.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val amount: Double,
    val balanceRemaining: Double,
    val date: String,
    val time: String
)
