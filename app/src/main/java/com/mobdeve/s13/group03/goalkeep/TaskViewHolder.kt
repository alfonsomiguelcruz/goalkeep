package com.mobdeve.s13.group03.goalkeep

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding

class TaskViewHolder(private val taskVB: TaskListLayoutBinding) : RecyclerView.ViewHolder(taskVB.root) {
    fun bindTaskData(t: Task) {
        taskVB.tvTaskName.text = t.name
        taskVB.tvTaskPriority.text = t.priority
        taskVB.tvTaskState.text = t.state
        taskVB.tvTaskTimeExpected.text = t.timeExpected

        taskVB.llTaskBody.setBackgroundResource(getTaskColor(t.priority))
        taskVB.tvTaskPriority.setTextColor(getDarkGrey())
        taskVB.tvTaskState.setBackgroundResource(getStateColors(t.state))
    }

    private fun getTaskColor (priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.drawable.corners_high
            "Medium" -> R.drawable.corners_medium
            "Low" -> R.drawable.corners_low
            else -> {
                R.drawable.corners_gt
            }
        }

        return color
    }

    private fun getTaskSubColor (priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.drawable.corners_sub_high
            "Medium" -> R.drawable.corners_sub_medium
            "Low" -> R.drawable.corners_sub_low
            else -> {
                R.drawable.corners_gt
            }
        }

        return color
    }

    private fun getStateColors(state: String): Int {
        val color: Int = when(state) {
            "Complete" -> R.drawable.corners_complete
            "Incomplete" -> R.drawable.corners_incomplete
            else -> {
                R.drawable.corners_gt
            }
        }

        return color
    }

    private fun getDarkGrey() : Int {
        return R.color.dark_grey
    }
}