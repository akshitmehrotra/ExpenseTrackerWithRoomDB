package com.example.expensetrackerwithroomdb

import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.expensetrackerwithroomdb.databinding.DialogEnterAmountBinding

class EnterAmountDialogFragment(
    private val onAmountEntered: (Double) -> Unit,
    private val isSettingInitialBudget: Boolean = false
) : DialogFragment() {

    private lateinit var binding: DialogEnterAmountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEnterAmountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set input filter to limit decimal places to 2
        binding.amountEditText.filters = arrayOf(DecimalDigitsInputFilter(2))

        binding.btnOk.setOnClickListener {
            val enteredAmount = binding.amountEditText.text.toString().toDoubleOrNull()
            if (enteredAmount != null && enteredAmount > 0) {
                // Pass the amount to the callback function
                onAmountEntered(enteredAmount)
                dismiss()
            } else {
                // Handle invalid input
                binding.amountEditText.error = "Enter a valid amount"
            }
        }

        // Update the dialog title if setting the initial budget
        if (isSettingInitialBudget) {
            binding.titleTextView.text = "Set Initial Budget"
        }

        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
    }
}

// Custom input filter to allow only 2 decimal places
class DecimalDigitsInputFilter(private val digitsAfterZero: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (source == ".") {
            if (dest?.contains(".") == true) {
                return ""
            }
        }
        if (dest?.contains(".") == true) {
            val splitArray = dest.toString().split(".")
            if (splitArray.size > 1) {
                val decimals = splitArray[1]
                if (decimals.length >= digitsAfterZero) {
                    return ""
                }
            }
        }
        return null
    }
}
