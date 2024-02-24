package com.mobdeve.s13.group03.goalkeep

import android.content.Context
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ViewGoalLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.tvViewGoalTitle.text = goal.name
        vb.tvViewGoalTimeExpected.text = goal.timeExpected
        vb.tvViewGoalPriority.text = goal.priority
        vb.tvViewGoalTag.text = goal.tag
        vb.tvViewGoalDescription.text = goal.description

        this.rv = vb.rvTasks
        this.tasksAdapter = TaskAdapter(this, tasks)
        this.rv.adapter = tasksAdapter

        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL
        vb.rvTasks.layoutManager = verticalLM
    }
}