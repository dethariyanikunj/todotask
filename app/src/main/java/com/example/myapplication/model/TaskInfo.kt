package com.example.myapplication.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class TaskInfo : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var task: String = ""
    var time: Long = 0
    var isSelected: Boolean = false
}