package com.mobdeve.s13.group03.goalkeep

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.ViewGoalLayoutBinding
import com.mobdeve.s13.group03.goalkeep.databinding.ViewTaskLayoutBinding

class ViewGoalActivity : AppCompatActivity() {
    private var goal : Goal = GoalDataGenerator.generateGoalData()[0]
    private var tasks : ArrayList<Task> = TaskDataGenerator.generateTaskData()
    private lateinit var vb : ViewGoalLayoutBinding
    private lateinit var rv : RecyclerView
    private lateinit var tasksAdapter: TaskAdapter

	companion object {
        const val ID_KEY = "ID_KEY"
        const val TITLE_KEY = "TITLE_KEY"
        const val TIME_KEY = "TIME_KEY"
        const val PRIORITY_KEY = "PRIORITY_KEY"
        const val TAG_KEY = "TAG_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
        const val PROGRESS_KEY = "PROGRESS_KEY"
        const val STATE_KEY = "STATE_KEY"
        const val GOAL_KEY: String = "GOAL_KEY"
    }
	
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ViewGoalLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val intent = intent
        val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(GOAL_KEY, Goal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(GOAL_KEY)
        }

        if (goal != null) {
            vb.tvViewGoalTitle.text = goal.name
            vb.tvViewGoalTimeExpected.text = goal.timeExpected
            vb.tvViewGoalPriority.text = goal.priority
            vb.tvViewGoalTag.text = goal.tag
            vb.tvViewGoalDescription.text = goal.description

            vb.llViewGoal.setBackgroundResource(getGoalColor(goal.priority))
            vb.pbViewGoal.setBackgroundResource(getGoalSubColor(goal.priority))
//            vb.tvViewGoalPriority.setTextColor(getGoalTextSubColor(goal.priority))
//            vb.tvViewGoalPriority.setLinkTextColor(getGoalTextSubColor(goal.priority))
//            vb.tvViewGoalPriority.setHintTextColor(getGoalTextSubColor(goal.priority))
//            vb.tvViewGoalPriority.highlightColor = getGoalTextSubColor(goal.priority)
            vb.tvViewGoalTag.background.setTint(getGoalSubColor("N/A"))
        }

        // TODO: Find way to edit display of button
        vb.btnCompleteGoal.isEnabled = false

        vb.fabEditGoal.setOnClickListener {
            val editGoalIntent = Intent(this, EditGoalActivity::class.java)
            editGoalIntent.putExtra(GOAL_KEY, goal)
            startActivity(editGoalIntent)
        }

        vb.fabAddTask.setOnClickListener{
            val addTaskIntent = Intent(this, AddTaskTitleActivity::class.java)
            startActivity(addTaskIntent)
        }

        this.rv = vb.rvTasks
        this.tasksAdapter = TaskAdapter(this, tasks)
        this.rv.adapter = tasksAdapter

        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL
        vb.rvTasks.layoutManager = verticalLM
    }

    private fun computeProgress(g: Goal): Int {
        var totalMatched: Int = 0
        var totalComplete: Int = 0
        var progress: Int

//        for(i in 0..<tasksList.size)
//            if(tasksList[i].goalId == g.goalId) {
//                totalMatched++
//                if (tasksList[i].state == "Complete")
//                    totalComplete++
//            }
        totalMatched = 3
        totalComplete = 5

        try {
            ((totalComplete * 100) / totalMatched).also { progress = it }
        } catch (e: ArithmeticException) {
            progress = 0
        }

        return progress
    }

    private fun getGoalColor(priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.drawable.regular_high
            "Medium" -> R.drawable.regular_medium
            "Low" -> R.drawable.regular_low
            else -> {
                R.drawable.corners_gt
            }
        }

        return color
    }

    private fun getGoalSubColor(priority: String): Int {
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

    private fun getGoalTextSubColor(priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.color.high_sub
            "Medium" -> R.color.medium_sub
            "Low" -> R.color.low_sub
            else -> {
                R.color.black
            }
        }

        return color
    }
}