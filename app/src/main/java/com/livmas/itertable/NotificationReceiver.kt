package com.livmas.itertable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.events.AlarmEvent
import org.greenrobot.eventbus.EventBus

class NotificationReceiver: BroadcastReceiver() {
    private lateinit var db: MainDB
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        db = MainDB.getDB(context!!)

        val item = intent?.getParcelableExtra("item", ListItem::class.java)!!
//        val coll = intent.getParcelableExtra("collection", CollectionParcelable::class.java)!!
        Toast.makeText(context,
            item.name,
            Toast.LENGTH_SHORT).show()
        Log.d("alert", item.name)

        if (ComplexCollectionActivity.isActive)
            EventBus.getDefault().post(AlarmEvent())
    }
}