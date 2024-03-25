package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val goals = GoalDataGenerator.generateGoalData()
    private val tasks = TaskDataGenerator.generateTaskData()
    private lateinit var vb : ActivityMainBinding
    private lateinit var rv : RecyclerView
    private lateinit var goalsAdapter : GoalAdapter
    private lateinit var goalsTouchHelper: ItemTouchHelper

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
        const val TIME_KEY = "TIME_KEY"
        const val TAG_KEY = "TAG_KEY"
        const val RESPONSE_KEY = "RESPONSE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.fabAddGoalList.setOnClickListener {
            val addGoalIntent = Intent(this, AddGoalTitleActivity::class.java)
            startActivity(addGoalIntent)
        }

        try {
            if (intent.getStringExtra(RESPONSE_KEY).toString() == "OKAY") {
                val goalTitle = intent.getStringExtra(TITLE_KEY).toString()
                val goalDescription = intent.getStringExtra(DESCRIPTION_KEY).toString()
                val goalTime = intent.getStringExtra(TIME_KEY).toString()
                val goalTag = intent.getStringExtra(TAG_KEY).toString()

                // Temporary Setting for Priority Assignment
                goals.add(
                    Goal(
                        goals.size + 1, goalTitle, "XXXX-XX-XX", goalTime,
                        "XXXX-XX-XX", goalDescription, "High", "Incomplete", goalTag
                    )
                )
            }
        } catch (e: Exception) {
            println("EXCEPTION FOUND!")
            println(e)
        }

        // Connecting Recycler View with Adapter
        // TODO: Pass the database contents of both goals and tasks to adapter
        this.rv = vb.rvGoals
        this.goalsAdapter = GoalAdapter(goals, tasks)
        this.rv.adapter = this.goalsAdapter

        // Orientation of the Activity Layout
        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL
        vb.rvGoals.layoutManager = verticalLM

        // Swipe Callback Function for the Items
        val goalSwipeCallback =
            GoalSwipeCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, this)
        goalSwipeCallback.goalAdapter = this.goalsAdapter
        goalsTouchHelper = ItemTouchHelper(goalSwipeCallback)
        goalsTouchHelper.attachToRecyclerView(this.rv)

        if (this.goalsAdapter.itemCount == 0) {
            vb.rvGoals.visibility = View.GONE
            vb.tvNogoalMsg.visibility = View.VISIBLE
        } else {
            vb.rvGoals.visibility = View.VISIBLE
            vb.tvNogoalMsg.visibility = View.GONE
        }

        vb.etGoalSearchbar.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                vb.ibGoalFilter.visibility = View.GONE
                vb.ibSettings.visibility = View.GONE
            } else {
                vb.ibGoalFilter.visibility = View.VISIBLE
                vb.ibSettings.visibility = View.VISIBLE
            }
        }
    }
}