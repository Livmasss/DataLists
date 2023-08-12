package com.livmas.itertable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.entities.CollectionParcelable
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.events.AlarmEvent
import org.greenrobot.eventbus.EventBus
import java.util.LinkedList

class NotificationReceiver: BroadcastReceiver() {
    private lateinit var db: MainDB
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        db = MainDB.getDB(context!!)

        var item: ListItem
        val coll = intent?.getParcelableExtra("collection", CollectionParcelable::class.java)!!
        val activeId = ComplexCollectionActivity.activeCollectionId

        if (activeId == coll.id) {
            EventBus.getDefault().post(AlarmEvent())
        }
        else {
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
            }.start()
        }
    }

    private fun updateNumbers(dataSet: LinkedList<ListItem>) {
        for (i in 0 until dataSet.size) {
            dataSet[i].number = i + 1
        }
    }
}