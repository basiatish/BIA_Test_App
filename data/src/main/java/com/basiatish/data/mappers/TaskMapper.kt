package com.basiatish.data.mappers

import com.basiatish.data.api.entities.TaskRemote
import com.basiatish.data.api.entities.TaskStatusRemote
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList
import com.basiatish.domain.entities.TaskStatus

class TaskMapper {

    fun toTask(taskRemote: TaskRemote): Task {
        return Task(
            id = taskRemote.id,
            status = taskRemote.status,
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

    fun toTaskList(taskListRemote: List<TaskRemote>): List<TaskList> {
        return taskListRemote.map { item ->
            TaskList(
                id = item.id,
                status = item.status,
                cargoType = item.cargoType,
                date = item.date,
                time = item.time,
                startPoint = item.startPoint,
                endPoint = item.endPoint,
                orderDetails = item.orderDetails,
                payDetails = item.payDetails
            )
        }
    }

    fun toTaskStatusRemote(status: String): TaskStatusRemote {
        return TaskStatusRemote(
            status = status
        )
    }

    fun toTaskStatus(taskStatusRemote: TaskStatusRemote): TaskStatus {
        return TaskStatus(
            status = taskStatusRemote.status
        )
    }
}