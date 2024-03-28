package com.mobdeve.s13.group03.goalkeep.model

import android.os.Parcel
import android.os.Parcelable

class Goal : Parcelable {
    var goalId : Int = -1
        private set

    var title: String
        private set

    var timeCreated: String
        private set

    var timeExpected: String
        private set

    var timeCompleted: String
        private set

    var description: String
        private set

    var tag: String
        private set

    var priority: String
        private set

    var state: String
        private set

    constructor(goalId: Int,
                title: String,
                timeCreated: String,
                timeExpected: String,
                timeCompleted: String,
                description: String,
                priority: String,
                state: String,
                tag: String) {
        this.goalId = goalId
        this.title = title
        this.timeCreated = timeCreated
        this.timeExpected = timeExpected
        this.timeCompleted = timeCompleted
        this.description = description
        this.priority = priority
        this.state = state
        this.tag = tag
    }

    constructor(title: String,
                timeCreated: String,
                timeExpected: String,
                timeCompleted: String,
                description: String,
                priority: String,
                state: String,
                tag: String) {
        this.title = title
        this.timeCreated = timeCreated
        this.timeExpected = timeExpected
        this.timeCompleted = timeCompleted
        this.description = description
        this.priority = priority
        this.state = state
        this.tag = tag
    }

    constructor(parcel: Parcel): this(
        goalId = parcel.readInt(),
        title = parcel.readString().toString(),
        timeCreated = parcel.readString().toString(),
        timeExpected = parcel.readString().toString(),
        timeCompleted = parcel.readString().toString(),
        description = parcel.readString().toString(),
        priority = parcel.readString().toString(),
        state = parcel.readString().toString(),
        tag = parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(this.goalId)
        parcel.writeString(this.title)
        parcel.writeString(this.timeCreated)
        parcel.writeString(this.timeExpected)
        parcel.writeString(this.timeCompleted)
        parcel.writeString(this.description)
        parcel.writeString(this.priority)
        parcel.writeString(this.state)
        parcel.writeString(this.tag)
    }

    companion object CREATOR : Parcelable.Creator<Goal> {
        override fun createFromParcel(parcel: Parcel): Goal {
            return Goal(parcel)
        }
        override fun newArray(size: Int): Array<Goal?> {
            return arrayOfNulls(size)
        }
    }
}