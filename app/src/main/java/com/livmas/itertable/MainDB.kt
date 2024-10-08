package com.livmas.itertable

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.livmas.itertable.entities.Alarm
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.entities.ListItem

@Database(entities = [CollectionItem::class, ListItem::class, Alarm::class],
    version = 4,
    autoMigrations = [])
abstract class MainDB: RoomDatabase() {
    abstract fun getDao(): Dao
    companion object {
        fun getDB(context: Context): MainDB {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "MainDB"
            ).build()
        }
    }

    fun insertCollectionFromDataModel(dataModel: DataModel): Long {
        val coll: CollectionItem = dataModel.newCollection.value ?: return 0
        val item = CollectionItem(
            null,
            coll.name,
            coll.type,
            coll.number)
        return getDao().insertColl(item)
    }

    fun deleteCollection(collection: CollectionItem) {
        Thread {
            if (collection.id == null) {
                throw IllegalStateException("Collection id is null!")
            }
            val itemsToDelete = getDao().getCollItems(collection.id!!)
            for (i in itemsToDelete) {
                deleteItem(i)
            }

            getDao().deleteColl(collection)
        }.start()
    }

    fun deleteItem(item: ListItem) {
        Thread {
            getDao().deleteItem(item)
        }.start()
    }
}