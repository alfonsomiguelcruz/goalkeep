package com.mobdeve.s13.group03.goalkeep

class Goal (goalId: Int, name: String, timeCreated: String, timeExpected: String,
            timeCompleted: String, description: String, priority: String, state: String, tag: String) {
    var goalId = goalId
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

    var tag = tag
        private set

    var priority = priority
        private set

    var state = state
        private set

}