package com.mobdeve.s13.group03.goalkeep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private val goals = GoalDataGenerator.generateGoalData()
    private val tasks = TaskDataGenerator.generateTaskData()
    private lateinit var rv : RecyclerView
    private lateinit var goalsAdapter : GoalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vb : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        this.rv = vb.rvGoals
        this.goalsAdapter = GoalAdapter(goals, tasks)
        this.rv.adapter =this.goalsAdapter

        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL

        vb.rvGoals.layoutManager = verticalLM
    }
}