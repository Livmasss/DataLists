package com.livmas.itertable.dialogs

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import java.util.Date

class MyDatePicker(context: Context, private val mutableLiveData: MutableLiveData<Date>): DatePickerDialog(context) {

    override fun onClick(dialog: DialogInterface, which: Int) {
        super.onClick(dialog, which)

        if (which == DialogInterface.BUTTON_POSITIVE) {
            datePicker.apply {
                val date = Date(
                    year,
                    month,
                    dayOfMonth
                )

                mutableLiveData.value = date
            }
        }
    }
}