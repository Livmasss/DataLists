package com.livmas.itertable

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.livmas.itertable.entities.Alarm
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.receivers.NotificationReceiver

class AlarmController(private val context: Context) {

    private val manager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    fun create(alarm: Alarm, collection: CollectionItem) {
        val intent = Intent(context, NotificationReceiver::class.java)
            .putExtra("collection", collection)
            .putExtra("alarm_info", alarm)

        sendAlarm(intent,
            alarm.lastCall,
            alarm.collectionId)
        Log.i("AlarmChannel", "Alarm started")
    }

    fun cancel(alarmId: Int) {
        context.getSystemService(AlarmManager::class.java).cancel(
            PendingIntent.getBroadcast(
                context.applicationContext,
                alarmId,
                Intent(context.applicationContext, NotificationReceiver::class.java),
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.i("AlarmChannel", "Alarm canceled")
    }

    fun cancel(alarm: Alarm) {
        context.getSystemService(AlarmManager::class.java).cancel(
            PendingIntent.getBroadcast(
                context.applicationContext,
                alarm.collectionId,
                Intent(context.applicationContext, NotificationReceiver::class.java),
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.i("AlarmChannel", "Alarm canceled")
    }

    private fun sendAlarm(intent: Intent, startTime: Long, alarmId: Int) {

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            startTime,
            pendingIntent
        )
    }
}