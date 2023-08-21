package com.livmas.itertable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.itertable.entities.CollectionItem
import java.util.Calendar

open class DataModel: ViewModel() {
    val newCollection: MutableLiveData<CollectionItem> by lazy {
        MutableLiveData<CollectionItem>()
    }

    val newListName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val editCollIndex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val editItemIndex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val editCollName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val editItemName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val startAlarmCalendar: MutableLiveData<Calendar> by lazy {
        MutableLiveData<Calendar>()
    }
    val repeatAlarmCalendar: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }
}