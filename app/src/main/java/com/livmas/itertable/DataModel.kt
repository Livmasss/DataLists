package com.livmas.itertable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.itertable.entities.CollectionType

open class DataModel: ViewModel() {
    val newCollName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val newCollType: MutableLiveData<CollectionType> by lazy {
        MutableLiveData<CollectionType>()
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
}