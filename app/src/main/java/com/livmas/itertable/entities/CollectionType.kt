package com.livmas.itertable.entities

enum class CollectionType(val id: Int) {
    List(0),
    Queue(1),
    Stack(2),
    Cycle(3),
    CheckList(4);

    companion object {
        fun valueOf(id: Int) = CollectionType.values().find {
            it.id == id
        }
    }
}