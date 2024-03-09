package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.TaskListLayoutBinding
import com.mobdeve.s13.group03.goalkeep.databinding.ViewTaskLayoutBinding
import java.nio.file.Files.size





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
        holder.itemView.setOnClickListener {
            println("Clicked Task " + this.tasks[position].name)
            showTask(tasks[position])
        }
    }

    private fun showTask(task: Task) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val vbTask = ViewTaskLayoutBinding.inflate(layoutInflater)
        val popUp = PopupWindow(context)

        val width = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.windowManager.currentWindowMetrics.bounds.width()
        } else {
            @Suppress("DEPRECATION")
            val display = context.windowManager.defaultDisplay
            val size = Point()
            @Suppress("DEPRECATION")
            display.getSize(size)
            size.x
        }

        popUp.contentView = vbTask.root
        popUp.width = width - 100
        popUp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popUp.isFocusable = true

//        val x = 200
//        val y = 60
        popUp.setBackgroundDrawable(ColorDrawable())
//        popUp.animationStyle = R.style.popup_window_animation

        vbTask.tvViewTaskTitle.text = task.name
        vbTask.tvViewTaskTimeExpected.text = task.timeExpected
        vbTask.tvViewTaskPriority.text = task.priority
        vbTask.tvViewTaskState.text = task.state
        vbTask.tvViewTaskDescription.text = task.description
        vbTask.llViewTask.clipToOutline = true

        vbTask.tvViewTaskPriority.background.setTint(getTaskColor(task.priority))
        vbTask.flTask.background.setTint(getTaskColor(task.priority))
        vbTask.tvViewTaskState.background.setTint(getStateColors(task.state))

        vbTask.fabViewTaskEdit.setOnClickListener{
            val editTaskIntent = Intent(this.context, EditGoalActivity::class.java)
//            startActivity(editTaskIntent)
        }

        popUp.showAtLocation(vbTask.root, Gravity.TOP, 0, 100)

        vbTask.btnCloseTask.setOnClickListener {
            popUp.dismiss()
        }
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