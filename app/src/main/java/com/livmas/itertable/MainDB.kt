package com.livmas.itertable

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.livmas.itertable.entities.dataBaseEntities.CollElems
import com.livmas.itertable.entities.dataBaseEntities.Colls

@Database(entities = [Colls::class, CollElems::class], version = 1)
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
}