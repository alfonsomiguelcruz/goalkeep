package com.mobdeve.s13.group03.goalkeep.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.DesignClass
import com.mobdeve.s13.group03.goalkeep.EditTaskActivity
import com.mobdeve.s13.group03.goalkeep.IntentKeys
import com.mobdeve.s13.group03.goalkeep.ViewGoalActivity
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding
import com.mobdeve.s13.group03.goalkeep.databinding.ViewTaskLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Task
import com.mobdeve.s13.group03.goalkeep.viewholder.TaskViewHolder


class TaskAdapter (private val tasks: ArrayList<Task>, private val activity: Activity) : RecyclerView.Adapter<TaskViewHolder>() {
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

        holder.itemView.setOnLongClickListener {
            holder.completeTask()
            editTaskState(tasks[position], position)
            false
        }
    }

    fun addTaskItem (task : Task, goalId : Int) {
        tasks.add(Task(
            task.taskId,
            task.name,
            task.timeCreated,
            task.timeExpected,
            task.timeCompleted,
            task.description,
            task.priority,
            task.state,
            goalId
        ))

        notifyItemInserted(tasks.size - 1)
    }

    fun editTaskState (t : Task, position : Int) {
        val db = GoalKeepDatabase(activity.applicationContext)
        db.updateTaskState(t.taskId)

        notifyItemChanged(position)
    }

    fun deleteTaskItem (position : Int) {
        val db = GoalKeepDatabase(activity.applicationContext)
        db.deleteTask(tasks[position].taskId)

        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        val size : Int = tasks.size
        tasks.clear()
        notifyItemRangeRemoved(0, size)
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
            view.context.startActivity(editTaskIntent)
        }

        vbTask.ibViewTaskClose.setOnClickListener {
            d.dismiss()
        }

        d.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        d.window?.setGravity(Gravity.TOP)

        vbTask.clViewTaskHeader.setBackgroundResource(DesignClass.getRegularColor(task.priority))
        vbTask.tvViewTaskPriority.setBackgroundResource(DesignClass.getCorneredColor(task.priority))
        vbTask.tvViewTaskStatus.setBackgroundResource(DesignClass.getStateColor(task.state))

        vbTask.tvViewTaskPriority.setTextColor(Color.WHITE)

        d.setCancelable(true)

        return d
    }
}