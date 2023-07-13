package com.livmas.itertable

import androidx.room.Dao
import androidx.room.Insert
import com.livmas.itertable.entities.dataBaseEntities.CollElems
import com.livmas.itertable.entities.dataBaseEntities.Colls

@Dao
interface Dao {
    @Insert
    fun insertColl(item: Colls): Long
    @Insert
    fun insertCollElem(item: CollElems)
}