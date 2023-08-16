package com.livmas.itertable.fragments

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.livmas.itertable.DataModel
import com.livmas.itertable.R
import com.livmas.itertable.databinding.SetAlarmBinding
import java.util.Calendar

class SetAlarmDialog: DialogFragment() {
    private lateinit var binding: SetAlarmBinding
    private val dataModel: DataModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SetAlarmBinding.inflate(layoutInflater)

        binding.apply {
            tpStart.setIs24HourView(true)
            tpRepeat.setIs24HourView(true)

            tpRepeat.hour = 0
            tpRepeat.minute = 0

            val now = Calendar.getInstance()
            tpStart.hour = now.get(Calendar.HOUR)
            tpStart.minute = now.get(Calendar.MINUTE)

            sRepeat.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    tpRepeat.visibility = View.VISIBLE
                    tvRepeat.visibility = View.VISIBLE
                }
                else {
                    tpRepeat.visibility = View.GONE
                    tvRepeat.visibility = View.GONE
                }
            }
        }

        val builder = Builder(activity)
        builder
            .setView(binding.root)
            .setMessage(R.string.set_alarm_dialog)
            .setPositiveButton(R.string.confirm) { _, _ ->
                confirmPick()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }

        return builder.create()
    }

    private fun confirmPick() {
        val start = Calendar.getInstance()
        var repeat: Long
        binding.apply {
            start.set(
                dpStart.year,
                dpStart.month,
                dpStart.dayOfMonth,
                tpStart.hour,
                tpStart.minute,
                0
            )
            repeat = ((tpRepeat.hour*60 + tpRepeat.minute) * 60000).toLong()
        }
        dataModel.repeatAlarmCalendar.value = repeat
        dataModel.startAlarmCalendar.value = start
    }
}