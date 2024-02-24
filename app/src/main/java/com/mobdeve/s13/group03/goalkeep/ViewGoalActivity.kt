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
        }

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
}