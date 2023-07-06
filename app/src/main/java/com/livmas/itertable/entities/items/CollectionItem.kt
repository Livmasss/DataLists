package com.livmas.itertable.entities.items

import com.livmas.itertable.entities.CollectionType

data class CollectionItem(var name: String, val type: CollectionType) {
    var number = 0
}