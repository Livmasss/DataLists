package com.livmas.itertable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.CollectionItem

open class DataModel: ViewModel() {
    val collName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val collType: MutableLiveData<CollectionType> by lazy {
        MutableLiveData<CollectionType>()
    }
    val collNumber: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val listName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}