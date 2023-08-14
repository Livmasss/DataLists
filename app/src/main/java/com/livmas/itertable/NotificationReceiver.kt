package com.livmas.itertable

import android.Manifest
import android.annotation.SuppressLint
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
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.entities.CollectionParcelable
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.events.AlarmEvent
import org.greenrobot.eventbus.EventBus
import java.util.LinkedList

class NotificationReceiver: BroadcastReceiver() {
    private lateinit var db: MainDB
    private lateinit var coll: CollectionParcelable
    private lateinit var item: ListItem
    private lateinit var context: Context
    private val channelId = "DataSets Channel"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        db = MainDB.getDB(context!!)
        this.context = context

        coll = intent?.getParcelableExtra("collection", CollectionParcelable::class.java)!!
        val activeId = ComplexCollectionActivity.activeCollectionId

        if (activeId == coll.id)
            EventBus.getDefault().post(AlarmEvent())
        else
            inactivePop()
    }

    private fun updateNumbers(dataSet: LinkedList<ListItem>) {
        for (i in 0 until dataSet.size) {
            dataSet[i].number = i + 1
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inactivePop() {
        Thread {
            val dataSet = LinkedList(coll.id?.let { db.getDao().getCollItems(it) }.orEmpty())

            when(CollectionType.valueOf(coll.typeId)) {
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
                    Log.d("alert", "Wrong collection type!")
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                Log.i("notification", "Please, enable notifications for application in settings!")
                return@Thread
            }
            sendNotification()
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