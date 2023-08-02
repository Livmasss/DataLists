package com.livmas.itertable.dialogs

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import androidx.lifecycle.MutableLiveData
import java.util.Calendar

class MyTimePicker(context: Context, private val mutableLiveData: MutableLiveData<Calendar>):
    TimePickerDialog(context, OnTimeSetListener { _, hourOfDay, minute ->
        val calendar = Calendar.getInstance()
            .apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
        mutableLiveData.value = calendar
    },
        0,
        0,
        true
    ) {

}