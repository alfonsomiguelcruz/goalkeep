package com.mobdeve.s13.group03.goalkeep.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.EditTaskActivity
import com.mobdeve.s13.group03.goalkeep.IntentKeys
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding
import com.mobdeve.s13.group03.goalkeep.databinding.ViewTaskLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Task
import com.mobdeve.s13.group03.goalkeep.viewholder.TaskViewHolder


class TaskAdapter (private val tasks: ArrayList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {
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
        holder.itemView.setOnClickListener { view ->
            displayTaskDialog(view.context as AppCompatActivity, tasks[position]).show()
        }
    }

    private fun displayTaskDialog (context: AppCompatActivity, task: Task) : Dialog {
        val d = Dialog(context)
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vbTask = ViewTaskLayoutBinding.inflate(layoutInflater)

        d.setContentView(vbTask.root)

        vbTask.tvViewTaskTitle.text = task.name
        vbTask.tvViewTaskTimeExpected.text = task.timeExpected
        vbTask.tvViewTaskPriority.text = task.priority
        vbTask.tvViewTaskStatus.text = task.state
        vbTask.tvViewTaskDescription.text = task.description

        vbTask.fabEditTask.setOnClickListener { view ->
            val editTaskIntent = Intent(view.context, EditTaskActivity::class.java)
            editTaskIntent.putExtra(IntentKeys.TASK_OBJECT_KEY.name, task)
            Log.d("MOBDEVE_MCO3", "Put Extra Done!")
            view.context.startActivity(editTaskIntent)
        }

        vbTask.ibViewTaskClose.setOnClickListener {
            d.dismiss()
        }

        d.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        d.window?.setGravity(Gravity.TOP)
//        d.window?.setBackgroundDrawableResource()
        d.setCancelable(true)

        return d
    }
}