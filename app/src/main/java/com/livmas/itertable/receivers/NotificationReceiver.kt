package com.livmas.itertable.receivers

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.livmas.itertable.AlarmController
import com.livmas.itertable.MainDB
import com.livmas.itertable.R
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.entities.Alarm
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.ListItem
import com.livmas.itertable.events.AlarmEvent
import org.greenrobot.eventbus.EventBus
import java.util.LinkedList
import kotlin.concurrent.thread

class NotificationReceiver: BroadcastReceiver() {
    private lateinit var db: MainDB
    private lateinit var coll: CollectionItem
    private lateinit var alarm: Alarm
    private lateinit var item: ListItem
    private lateinit var context: Context
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmController: AlarmController

    private val channelId = "DataSets Channel"
    private val tag = "AlarmChannel"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(tag, "Alarm received")
        if (context == null) {
            Log.w(tag, "Null context")
            return
        }
        if (intent == null) {
            Log.w(tag, "Null intent")
            return
        }
        val extras = intent.extras
        if (extras == null) {
            Log.w(tag, "Null extras")
            return
        }

        this.context = context.applicationContext
        db = MainDB.getDB(this.context)
        alarmManager = this.context.getSystemService(AlarmManager::class.java)
        alarmController = AlarmController(this.context)

        extras.getParcelable("collection", CollectionItem::class.java).let {
            if (it == null) {
                Log.w(tag, "No collection extra")
                return
            }
            coll = it
        }
        extras.getParcelable("alarm_info", Alarm::class.java).let {
            if (it == null) {
                Log.w(tag, "Null alarm_info extra")
                return
            }
            alarm = it
        }

        val activeId = ComplexCollectionActivity.activeCollectionId
        if (activeId == coll.id)
            EventBus.getDefault().post(AlarmEvent())
        else
            inactivePop()

        thread {
            refreshDB()
            alarm.apply {
                lastCall += repeat

                if (repeat > 10000)
                    resetAlarm()
            }
        }
    }

    private fun refreshDB() {
        if (alarm.repeat <= (0).toLong()) {
            db.getDao().deleteAlarm(alarm)
        }
        else {
            alarm.lastCall = System.currentTimeMillis()
            db.getDao().updateAlarm(alarm)
        }
    }

    private fun updateNumbers(dataSet: LinkedList<ListItem>) {
        for (i in 0 until dataSet.size)
            dataSet[i].number = i + 1
    }

    private fun resetAlarm() {
        alarmController.create(alarm, coll)
        Log.d(tag, "Alarm of collection ${coll.id} relaunched")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inactivePop() {
         Thread {
            val dataSet = LinkedList(coll.id?.let { db.getDao().getCollItems(it) }.orEmpty())

            when(coll.type) {
                CollectionType.Queue -> {
                    item = dataSet[0]

                    dataSet.removeAt(0)
                    updateNumbers(dataSet)

                    db.getDao().deleteItem(item)
                    dataSet.forEach {
                        db.getDao().updateItem(it)
                    }
                }

                CollectionType.Stack -> {
                    item = dataSet[dataSet.size - 1]
                    db.getDao().deleteItem(item)
                }

                CollectionType.Cycle -> {
                    item = dataSet[0]

                    dataSet.removeAt(0)
                    dataSet.add(item)
                    updateNumbers(dataSet)

                    dataSet.forEach {
                        db.getDao().updateItem(it)
                    }
                }
                else -> {
                    Log.d(tag, "Wrong collection type!")
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(tag, "Please, enable notifications for application in settings!")
                return@Thread
            }
            sendNotification()
            Log.i(tag, "Notification sent")
        }.start()

    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification() {
        val contentText = context.resources.getString(R.string.pop_notification, coll.name)
        createChannel()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.pop_change)
            .setContentTitle(item.name)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(context)) {
            item.id?.let {
                notify(it, notification) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val description = context.getString(R.string.notification_channel_description)
        val channel = NotificationChannel(channelId, channelId, importance)

        channel.description = description
        manager.createNotificationChannel(channel)
    }
}