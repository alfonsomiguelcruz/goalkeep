package com.mobdeve.s13.group03.goalkeep.model

import android.os.Parcel
import android.os.Parcelable

class Task : Parcelable {
    var taskId : Int = -1
        private set

    var name : String
        private set

    var timeCreated : String
        private set

    var timeExpected : String
        private set

    var timeCompleted : String
        private set

    var description : String
        private set

    var priority : String
        private set

    var state : String
        private set

    var goalId : Int = -1
        private set

    constructor(taskId: Int,
                name: String,
                timeCreated: String,
                timeExpected: String,
                timeCompleted: String,
                description: String,
                priority: String,
                state: String,
                goalId: Int) {
        this.taskId = taskId
        this.name = name
        this.timeCreated = timeCreated
        this.timeExpected = timeExpected
        this.timeCompleted = timeCompleted
        this.description = description
        this.priority = priority
        this.state = state
        this.goalId = goalId
    }

    constructor(name: String,
                timeCreated: String,
                timeExpected: String,
                timeCompleted: String,
                description: String,
                priority: String,
                state: String,
                goalId: Int) {
        this.name = name
        this.timeCreated = timeCreated
        this.timeExpected = timeExpected
        this.timeCompleted = timeCompleted
        this.description = description
        this.priority = priority
        this.state = state
        this.goalId = goalId
    }

    constructor(parcel: Parcel): this(
        taskId = parcel.readInt(),
        name = parcel.readString().toString(),
        timeCreated = parcel.readString().toString(),
        timeExpected = parcel.readString().toString(),
        timeCompleted = parcel.readString().toString(),
        description = parcel.readString().toString(),
        priority = parcel.readString().toString(),
        state = parcel.readString().toString(),
        goalId = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(this.taskId)
        parcel.writeString(this.name)
        parcel.writeString(this.timeCreated)
        parcel.writeString(this.timeExpected)
        parcel.writeString(this.timeCompleted)
        parcel.writeString(this.description)
        parcel.writeString(this.priority)
        parcel.writeString(this.state)
        parcel.writeInt(this.goalId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }
        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}