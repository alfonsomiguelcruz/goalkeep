package com.mobdeve.s13.group03.goalkeep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding

class TaskAdapter (private val context: AppCompatActivity, private val tasks: ArrayList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskViewBinding : TaskListLayoutBinding = TaskListLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(taskViewBinding)
    }

    override fun getItemCount(): Int {
        return this.tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindTaskData(tasks[position])
    }
}