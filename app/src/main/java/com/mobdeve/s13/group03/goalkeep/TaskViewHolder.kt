package com.mobdeve.s13.group03.goalkeep

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding

class TaskViewHolder(private val taskVB: TaskListLayoutBinding) : RecyclerView.ViewHolder(taskVB.root) {
    fun bindTaskData(t: Task) {
        taskVB.tvTaskName.text = t.name
        taskVB.tvTaskPriority.text = t.priority
        taskVB.tvTaskState.text = t.state
        taskVB.tvTaskTimeExpected.text = t.timeExpected

        taskVB.llTaskBody.setBackgroundResource(DesignClass.getCorneredColor(t.priority))
        taskVB.tvTaskPriority.setTextColor(DesignClass.getDarkGray())
        taskVB.tvTaskState.setBackgroundResource(DesignClass.getStateColor(t.state))
    }
}