package com.example.myapplication.db

import com.example.myapplication.model.TaskInfo
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

object DbUtils {

    private val config = RealmConfiguration.create(schema = setOf(TaskInfo::class))

    fun saveTasks(taskTitle: String, taskTime: Long) {
        val realm: Realm = Realm.open(config)
        realm.writeBlocking {
            copyToRealm(
                TaskInfo().apply {
                    task = taskTitle
                    time = taskTime
                },
            )
        }
    }

    fun markTaskCompleted(info: TaskInfo, isCompleted: Boolean) {
        val realm: Realm = Realm.open(config)
        realm.writeBlocking {
            findLatest(info)?.isSelected = isCompleted
        }
    }

    fun retrieveTasks(): RealmResults<TaskInfo> {
        val realm: Realm = Realm.open(config)
        return realm.query<TaskInfo>().find()
    }

    fun retrieveDateWiseTasks(startTime: Long, endTime: Long): RealmResults<TaskInfo> {
        val realm: Realm = Realm.open(config)
        return realm.query<TaskInfo>("time >= $startTime AND time <= $endTime").find()
    }

    fun deleteTask(info: TaskInfo) {
        val realm: Realm = Realm.open(config)
        realm.writeBlocking {
            val writeTransactionItems = query<TaskInfo>().find()
            if (writeTransactionItems.isNotEmpty()) {
                val toDelete = writeTransactionItems.first { it._id == info._id }
                delete(toDelete)
            }
        }
    }
}