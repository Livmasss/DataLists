package com.livmas.itertable.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.livmas.itertable.NotificationReceiver
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ActivityComplexCollectionBinding
import com.livmas.itertable.dialogs.MyTimePicker
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.events.AlarmEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class ComplexCollectionActivity: CollectionActivity() {
    protected lateinit var binding: ActivityComplexCollectionBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            ibSetAlarm.setOnClickListener(
                alarmOnClickListener()
            )
        }

        initRecycler(binding.rvContent)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    private fun alarmOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            MyTimePicker(this, dataModel.alertCalendar).show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onAlarmEvent(event: AlarmEvent) {
        adapter.pop()
    }

    override fun setObservers() {
        super.setObservers()

        dataModel.apply {
            alertCalendar.observe(this@ComplexCollectionActivity) { calendar ->
                val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

                val intent = Intent(
                    this@ComplexCollectionActivity, NotificationReceiver::class.java)
                    .putExtra("item", adapter.getItem())
                    .putExtra("collection", collInfo)

                val pendingIntent = PendingIntent.getBroadcast(
                    this@ComplexCollectionActivity,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }
}