package com.livmas.itertable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.livmas.itertable.entities.CollectionParcelable
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.ListItem

class NotificationReceiver: BroadcastReceiver() {
    private lateinit var db: MainDB
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        db = MainDB.getDB(context!!)

        val item = intent?.getParcelableExtra("item", ListItem::class.java)!!
        val coll = intent.getParcelableExtra("collection", CollectionParcelable::class.java)!!
        Toast.makeText(context,
            item.name,
            Toast.LENGTH_SHORT).show()
        Log.d("alert", item.name)

        if (CollectionType.valueOf(coll.typeId) != CollectionType.Cycle) {
            db.deleteItem(item)

            if (CollectionType.valueOf(coll.typeId) == CollectionType.Queue) {
                val cycleItems = db.getDao().getCollItems(coll.id!!)
                Thread{
                    cycleItems.forEach {
                        it.number--
                        db.getDao().updateItem(it)
                    }
                }.start()
            }
            return
        }

        Thread {
            val cycleItems = db.getDao().getCollItems(coll.id!!)
            cycleItems.forEach {
                it.number--
                if (it.number == 0)
                    it.number = cycleItems.size
                db.getDao().updateItem(it)
            }
        }.start()
    }
}