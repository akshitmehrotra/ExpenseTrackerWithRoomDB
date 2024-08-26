package com.example.expensetrackerwithroomdb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.expensetrackerwithroomdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var budgetViewModel: BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        // Observe the current balance and update the UI
        budgetViewModel.currentBalance.observe(this) { balance ->
            binding.remainingAmountText.text = "₹$balance"
        }

        budgetViewModel.currentBalance.observe(this) { balance ->
            binding.remainingAmountText.text = "₹$balance"
            val percentage = if (budgetViewModel.totalBudget.value != 0.0) {
                (balance / budgetViewModel.totalBudget.value!!) * 100
            } else {
                0.0
            }


            // Update circular progress bar
            binding.circularProgressIndicator.progress = percentage.toInt()
        }


        budgetViewModel.totalBudget.observe(this) { totalBudget ->
            if (totalBudget == 0.0) {
                showInitialBudgetDialog()
            }
        }

        // Set up deposit button to show the enter amount dialog
        binding.depositButton.setOnClickListener {
            showEnterAmountDialog { amount ->
                budgetViewModel.updateBudget(amount)
            }
        }

        // Set up withdraw button to show the enter amount dialog
        binding.withdrawButton.setOnClickListener {
            showEnterAmountDialog { amount ->
                budgetViewModel.updateBudget(-amount)
            }
        }

        // Navigate to the ExpenseHistoryActivity
        binding.expenseHistoryButton.setOnClickListener {
            val intent = Intent(this, ExpenseHistoryActivity::class.java)
            startActivity(intent)
        }

        // Set up the settings button to show the dialog for setting the initial budget
        binding.settingsButton.setOnClickListener {
            val dialog = EnterAmountDialogFragment(
                onAmountEntered = { amount ->
                    budgetViewModel.setInitialBudget(amount)
                },
                isSettingInitialBudget = true
            )
            dialog.show(supportFragmentManager, "EnterAmountDialog")
        }
    }

    private fun showInitialBudgetDialog() {
        val dialog = EnterAmountDialogFragment(
            onAmountEntered = { amount ->
                budgetViewModel.setInitialBudget(amount)
            },
            isSettingInitialBudget = true
        )
        dialog.show(supportFragmentManager, "EnterAmountDialog")
    }

    // Method to show the enter amount dialog
    private fun showEnterAmountDialog(onAmountEntered: (Double) -> Unit) {
        val dialog = EnterAmountDialogFragment(onAmountEntered)
        dialog.show(supportFragmentManager, "EnterAmountDialog")
    }
}
