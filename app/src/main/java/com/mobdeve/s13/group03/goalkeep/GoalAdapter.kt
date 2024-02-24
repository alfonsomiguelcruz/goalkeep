package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding

class GoalAdapter(private val context: AppCompatActivity, private val goals: ArrayList<Goal>, private val tasksList: ArrayList<Task>) : RecyclerView.Adapter<GoalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val goalViewBinding : GoalListLayoutBinding = GoalListLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        val tempViewHolder = GoalViewHolder(goalViewBinding, tasksList)

        tempViewHolder.itemView.setOnClickListener{
            val goalInt = Intent(parent.context, ViewGoalActivity::class.java)
            // Title of the Goal
            goalInt.putExtra(ViewGoalActivity.TITLE_KEY, goalViewBinding.tvGoalName.text.toString())
            // Time Expected to complete the Goal
            goalInt.putExtra(ViewGoalActivity.TIME_KEY, goalViewBinding.tvGoalTimeExpected.text.toString())
            // Priority of the Goal
            goalInt.putExtra(ViewGoalActivity.PRIORITY_KEY, goalViewBinding.tvGoalPriority.toString())
            // Tag of the Goal
            goalInt.putExtra(ViewGoalActivity.TAG_KEY, goalViewBinding.tvGoalTag.text.toString())
            // Description of the Goal
            goalInt.putExtra(ViewGoalActivity.DESCRIPTION_KEY, goals[tempViewHolder.bindingAdapterPosition].description)
            // Get the progress percentage (min: 0, max: 100)
            goalInt.putExtra(ViewGoalActivity.PROGRESS_KEY, computeProgress(goals[tempViewHolder.bindingAdapterPosition]))
            // Get the state of the progress, used to enable the button that completes a goal
            goalInt.putExtra(ViewGoalActivity.STATE_KEY, computeProgress(goals[tempViewHolder.bindingAdapterPosition]) == 100)

            /* TODO: Find a way to Send the Intent to ViewGoalActivity */
        }

        return tempViewHolder
    }

    override fun getItemCount(): Int {
        return this.goals.size
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bindGoalData(this.goals[position])
        holder.itemView.setOnClickListener {
            println("Clicked Goal " + this.goals[position].name)
            val viewGoalIntent = Intent(context, ViewGoalActivity::class.java)
            viewGoalIntent.putExtra("goal_position", position)
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