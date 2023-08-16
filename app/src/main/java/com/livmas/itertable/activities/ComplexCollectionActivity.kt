package com.livmas.itertable.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.livmas.itertable.NotificationReceiver
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ActivityComplexCollectionBinding
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.events.AlarmEvent
import com.livmas.itertable.fragments.AlarmFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Calendar

abstract class ComplexCollectionActivity: CollectionActivity() {
    companion object {
        var activeCollectionId = 0
    }

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
        activeCollectionId = collInfo.id!!
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        activeCollectionId = 0
    }

    private fun alarmOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val fragment = AlarmFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fcContainer, fragment, "alarm")
                .commit()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onAlarmEvent(event: AlarmEvent) {
        adapter.pop()
        Log.i("alarm", "Alarm event occurred.")
    }

    override fun setObservers() {
        super.setObservers()

        dataModel.apply {
            startAlarmCalendar.observe(this@ComplexCollectionActivity) { startCalendar ->

                val fragment = binding.fcContainer.getFragment<AlarmFragment>()
                fragment.binding.apply {
                    tvIsActive.text = getString(R.string.active)
                    tvStartTime.text = getString(R.string.time_template,
                        startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE))
                    tvStartDate.text = getString(R.string.date_template,
                        startCalendar.get(Calendar.DATE), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.YEAR))
                }

                val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

                val intent = Intent(
                    this@ComplexCollectionActivity, NotificationReceiver::class.java)
                    .putExtra("collection", collInfo)

                val pendingIntent = PendingIntent.getBroadcast(
                    this@ComplexCollectionActivity,
                    collInfo.id!!,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                val repeat = repeatAlarmCalendar.value
                if (repeat == null || repeat == (0).toLong()) {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        startCalendar.timeInMillis,
                        pendingIntent
                    )

                    fragment.binding.tvRepeatTime.text = getString(R.string.nul)
                }
                else {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        startCalendar.timeInMillis,
                        repeat,
                        pendingIntent
                    )

                    var minutes: Int
                    val hours: Int
                    with(repeat/1000) {
                        minutes = (this/60).toInt()
                        hours = minutes/60
                        minutes %= 60
                    }
                    fragment.binding.tvRepeatTime.text = getString(
                        R.string.time_template, hours, minutes)
                }
            }
        }
    }
}