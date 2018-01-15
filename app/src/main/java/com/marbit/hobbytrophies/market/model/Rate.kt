package com.marbit.hobbytrophies.market.model

import com.google.firebase.database.ServerValue
import com.google.firebase.database.Exclude



class Rate {
    lateinit var id: String
    var type: String = ""
    var value: Int = 0
    var comment: String = ""
    var author: String = ""
    lateinit var userId: String
    lateinit var creationDate: Any
    lateinit var itemId: String

    constructor(){
        this.creationDate = ServerValue.TIMESTAMP
    }

    constructor(userId: String, type: String, author: String, itemId: String){
        this.userId = userId
        this.type = type
        this.author = author
        this.itemId = itemId
        this.creationDate = ServerValue.TIMESTAMP
    }

    @Exclude
    fun getCreatedTimestampLong(): Long {
        return creationDate as Long
    }
}
