package com.livmas.itertable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.itertable.entities.CollectionType

open class DataModel: ViewModel() {
    val collectionName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val collectionType: MutableLiveData<CollectionType> by lazy {
        MutableLiveData<CollectionType>()
    }
    val collectionId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
}