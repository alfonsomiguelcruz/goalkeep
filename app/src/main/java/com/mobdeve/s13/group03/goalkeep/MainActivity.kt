package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val goals = GoalDataGenerator.generateGoalData()
    private val tasks = TaskDataGenerator.generateTaskData()
    private lateinit var vb : ActivityMainBinding
    private lateinit var rv : RecyclerView
    private lateinit var goalsAdapter : GoalAdapter

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
        const val TIME_KEY = "TIME_KEY"
        const val TAG_KEY = "TAG_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.fabAddGoalList.setOnClickListener {
            println("Clicked FAB")
            val addGoalIntent = Intent(this, AddGoalTitleActivity::class.java)
            startActivity(addGoalIntent)

        }

        this.rv = vb.rvGoals
        this.goalsAdapter = GoalAdapter(goals, tasks)
        this.rv.adapter =this.goalsAdapter

        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL
        vb.rvGoals.layoutManager = verticalLM
    }
}