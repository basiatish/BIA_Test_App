package com.basiatish.datamodule.mappers

import com.basiatish.datamodule.api.entities.TaskRemote
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList

class TaskMapper {

    fun toTask(taskRemote: TaskRemote): Task {
        return Task(
            cargoType = taskRemote.cargoType,
            city = taskRemote.city,
            date = taskRemote.date,
            time = taskRemote.time,
            startPoint = taskRemote.startPoint,
            endPoint = taskRemote.endPoint,
            bodyType = taskRemote.bodyType,
            orderDetails = taskRemote.orderDetails,
            payDetails = taskRemote.payDetails,
            phone = taskRemote.phone,
            name = taskRemote.name
        )
    }

    fun toListTask(taskListRemote: List<TaskRemote>): List<Task> {
        return taskListRemote.map { item ->
            Task(
                cargoType = item.cargoType,
                city = item.city,
                date = item.date,
                time = item.time,
                startPoint = item.startPoint,
                endPoint = item.endPoint,
                bodyType = item.bodyType,
                orderDetails = item.orderDetails,
                payDetails = item.payDetails,
                phone = item.phone,
                name = item.name
            )
        }
    }

    fun toTaskList(taskListRemote: List<TaskRemote>): List<TaskList> {
        return taskListRemote.map { item ->
            TaskList(
                cargoType = item.cargoType,
                date = item.date,
                time = item.time,
                orderDetails = item.orderDetails,
                payDetails = item.payDetails
            )
        }
    }
}