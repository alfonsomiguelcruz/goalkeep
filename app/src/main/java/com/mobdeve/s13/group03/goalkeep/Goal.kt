package com.mobdeve.s13.group03.goalkeep

import android.os.Parcel
import android.os.Parcelable

class Goal (goalId: Int,
            title: String,
            timeCreated: String,
            timeExpected: String,
            timeCompleted: String,
            description: String,
            priority: String,
            state: String,
            tag: String) : Parcelable {
    var goalId = goalId
        private set

    var title = title
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