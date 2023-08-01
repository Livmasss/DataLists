package com.livmas.itertable.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.livmas.itertable.NotificationReceiver
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ActivityComplexCollectionBinding
import com.livmas.itertable.dialogs.MyDatePicker
import com.livmas.itertable.entities.CollectionType

abstract class ComplexCollectionActivity: CollectionActivity() {
    protected lateinit var binding: ActivityComplexCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplexCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            tvTitle.text = resources.getString(R.string.colon, collInfo.name)
            tvType.text = CollectionType.valueOf(collInfo.typeId).toString()

            fabNewItem.setOnClickListener {
                startNewListDialog()
            }
            bBack.setOnClickListener {
                finish()
            }
            ibSetAlarm.setOnClickListener(alarmOnClickListener())
        }

        initRecycler(binding.rvContent)
    }

    private fun alarmOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            MyDatePicker(this, dataModel.alertDate).show()
        }
    }

    override fun setObservers() {
        super.setObservers()

        dataModel.alertDate.observe(this) { date ->
            val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            val intent = Intent(this, NotificationReceiver::class.java)
                .putExtra("message", adapter.pop()?.name)

            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 100000,
                pendingIntent)
        }
    }
}