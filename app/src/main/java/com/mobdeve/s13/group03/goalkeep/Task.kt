package com.mobdeve.s13.group03.goalkeep

import android.os.Parcel
import android.os.Parcelable

class Task (taskId: Int,
            name: String,
            timeCreated: String,
            timeExpected: String,
            timeCompleted: String,
            description: String,
            priority: String,
            state: String,
            goalId: Int) : Parcelable {
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

    override fun describeContents(): Int {
        return 0
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

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }
        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}