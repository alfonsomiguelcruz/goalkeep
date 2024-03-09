package com.mobdeve.s13.group03.goalkeep

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding

class TaskViewHolder(private val taskVB: TaskListLayoutBinding) : RecyclerView.ViewHolder(taskVB.root) {
    fun bindTaskData(t: Task) {
        taskVB.tvTaskName.text = t.name
        taskVB.tvTaskPriority.text = t.priority
        taskVB.tvTaskState.text = t.state
        taskVB.tvTaskTimeExpected.text = t.timeExpected

        taskVB.llTaskBody.background.setTint(getTaskColor(t.priority))
        taskVB.tvTaskPriority.setTextColor(getTaskSubColor(t.priority))
        taskVB.tvTaskState.background.setTint(getStateColors(t.state))
    }

    private fun getTaskColor(priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.color.high_default
            "Medium" -> R.color.medium_default
            "Low" -> R.color.low_default
            else -> {
                R.drawable.corners_gt
            }
        }

        return color
    }

    private fun getTaskSubColor(priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.color.high_sub
            "Medium" -> R.color.medium_sub
            "Low" -> R.color.low_sub
            else -> {
                R.color.white
            }
        }

        return color
    }

    private fun getStateColors(state: String): Int {
        val color: Int = when(state) {
            "Complete" -> R.color.complete
            "Incomplete" -> R.color.incomplete
            else -> {
                R.color.white
            }
        }

        return color
    }
}