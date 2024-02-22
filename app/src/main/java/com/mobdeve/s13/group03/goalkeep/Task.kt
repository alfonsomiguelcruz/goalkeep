package com.mobdeve.s13.group03.goalkeep

class Task (taskId: Int, name: String, timeCreated: String, timeExpected: String,
            timeCompleted: String, description: String, priority: String, state: String, goalId: Int) {
    var taskId = taskId
        private set

    var name = name
        private set

    var timeCreated = timeCreated
        private set

    var timeExpected = timeExpected
        private set

    var timeCompleted = timeCompleted
        private set

    var description = description
        private set

    var priority = priority
        private set

    var state = state
        private set

    var goalId = goalId
        private set
}