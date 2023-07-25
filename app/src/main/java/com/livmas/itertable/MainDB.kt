package com.livmas.itertable

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.CollectionItem
import com.livmas.itertable.entities.items.ListItem
import java.lang.IllegalStateException

@Database(entities = [CollectionItem::class, ListItem::class],
    version = 8,
    autoMigrations = [
        AutoMigration(from = 7, to = 8)
    ])
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
        var type = dataModel.newCollType.value
        if (type == null) {
            type = CollectionType.List
        }
        val item = CollectionItem(
            null,
            dataModel.newCollName.value.orEmpty(),
            type)
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