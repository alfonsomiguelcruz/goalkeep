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
import kotlin.math.roundToInt

class ViewGoalActivity : AppCompatActivity() {
    private var goal : Goal = GoalDataGenerator.generateGoalData()[0]
    private var tasks : ArrayList<Task> = TaskDataGenerator.generateTaskData()
    private lateinit var vb : ViewGoalLayoutBinding
    private lateinit var rv : RecyclerView
    private lateinit var tasksAdapter: TaskAdapter
	
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ViewGoalLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val intent = intent
        val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name, Goal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name)
        }

        if (goal != null) {
            vb.tvViewGoalTitle.text = goal.title
            vb.tvViewGoalTimeExpected.text = goal.timeExpected
            vb.tvViewGoalPriority.text = goal.priority
            vb.tvViewGoalTag.text = goal.tag
            vb.tvViewGoalDescription.text = goal.description
            vb.llViewGoal.setBackgroundResource(DesignClass.getRegularColor(goal.priority))
            vb.pbViewGoal.setBackgroundResource(DesignClass.getSubColor(goal.priority))
            vb.tvViewGoalTag.background.setTint(DesignClass.getSubColor("N/A"))
        }

        // TODO: Find way to edit display of button
        vb.btnCompleteGoal.isEnabled = false

        vb.fabEditGoal.setOnClickListener {
            val editGoalIntent = Intent(this, EditGoalActivity::class.java)
            editGoalIntent.putExtra(IntentKeys.GOAL_OBJECT_KEY.name, goal)
            startActivity(editGoalIntent)
        }

        vb.fabAddTask.setOnClickListener {
            val addTaskIntent = Intent(this, AddTaskTitleActivity::class.java)
            startActivity(addTaskIntent)
        }

        this.rv = vb.rvTasks
        this.tasksAdapter = TaskAdapter(tasks)
        this.rv.adapter = tasksAdapter

        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL
        vb.rvTasks.layoutManager = verticalLM


    }

    private fun computeProgress(g: Goal): Int {
        var totalMatched: Int = 0
        var totalComplete: Int = 0

//        for(i in 0..<tasksList.size)
//            if(tasksList[i].goalId == g.goalId) {
//                totalMatched++
//                if (tasksList[i].state == "Complete")
//                    totalComplete++
//            }
        totalMatched = 10
        totalComplete = 5

        val progress: Int = try {
            ((totalComplete * 1.0 / totalMatched) * 100).roundToInt()
        } catch (e: ArithmeticException) {
            0
        }

        return progress
    }
}