package com.mobdeve.s13.group03.goalkeep.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.DesignClass
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Task

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