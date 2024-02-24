package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding

class GoalAdapter(private val goals: ArrayList<Goal>, private val tasksList: ArrayList<Task>) : RecyclerView.Adapter<GoalViewHolder>() {
    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val goalViewBinding : GoalListLayoutBinding = GoalListLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        val tempViewHolder = GoalViewHolder(goalViewBinding, tasksList)
        this.parent = parent

        return tempViewHolder
    }

    override fun getItemCount(): Int {
        return this.goals.size
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bindGoalData(this.goals[position])
        holder.itemView.setOnClickListener {
            println("Clicked Goal " + this.goals[position].name)
            val context = parent.context

            val viewGoalIntent = Intent(parent.context, ViewGoalActivity::class.java)
            viewGoalIntent.putExtra("GOAL_KEY", this.goals[position])

            context.startActivity(viewGoalIntent)
        }
    }

    private fun computeProgress(g: Goal): Int {
        var totalMatched: Int = 0
        var totalComplete: Int = 0
        var progress: Int

        for(i in 0..<tasksList.size)
            if(tasksList[i].goalId == g.goalId) {
                totalMatched++
                if (tasksList[i].state == "Complete")
                    totalComplete++
            }

        try {
            ((totalComplete * 100) / totalMatched).also { progress = it }
        } catch (e: ArithmeticException) {
            (-1).also { progress = it }
        }

        return progress
    }
}