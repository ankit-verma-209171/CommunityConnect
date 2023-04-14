package com.codeitsolo.communityconnect.utils

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun DatePicker(date: String, onDateChange: (String) -> Unit, label: @Composable () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateText by rememberSaveable {
        mutableStateOf(date)
    }

    // Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )

    ReadonlyTextField(
        value = selectedDateText,
        onValueChange = onDateChange,
        onClick = { datePicker.show() },
        label = label
    )
}
