package com.livmas.itertable

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.CollectionItem
import java.util.UUID

@Database(entities = [CollectionItem::class], version = 6)
abstract class MainDB: RoomDatabase() {
    abstract fun getDao(): Dao
    companion object {
        fun getDB(context: Context): MainDB {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "MainDB"
            ).fallbackToDestructiveMigration().build()
        }
    }

    fun insertThread(dataModel: DataModel) {
        Thread {
            var type = dataModel.collectionType.value
            if (type == null) {
                type = CollectionType.List
            }
            val item = CollectionItem(
                null,
                dataModel.collectionName.value.orEmpty(),
                type)
            getDao().insertColl(item)
        }.start()
    }

    fun deleteThread(collection: CollectionItem) {
        Thread {
            getDao().deleteColl(collection)
        }.start()
    }
}