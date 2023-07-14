package com.livmas.itertable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.livmas.itertable.entities.dataBaseEntities.CollElems
import com.livmas.itertable.entities.dataBaseEntities.Colls
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertColl(item: Colls): Long
    @Insert
    fun insertCollElem(item: CollElems)
    @Query("SELECT * FROM collections")
    fun getAllColls(): List<Colls>
    @Delete
    fun deleteColl(item: Colls)
}