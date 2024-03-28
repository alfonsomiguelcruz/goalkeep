package com.mobdeve.s13.group03.goalkeep.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.IntentKeys
import com.mobdeve.s13.group03.goalkeep.ViewGoalActivity
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import com.mobdeve.s13.group03.goalkeep.model.Task
import com.mobdeve.s13.group03.goalkeep.viewholder.GoalViewHolder

class GoalAdapter(private val goals: ArrayList<Goal>, private val tasksList: ArrayList<Task>) : RecyclerView.Adapter<GoalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val goalViewBinding : GoalListLayoutBinding = GoalListLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return GoalViewHolder(goalViewBinding, tasksList)
    }

    override fun getItemCount(): Int {
        return this.goals.size
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bindGoalData(this.goals[position])
        holder.itemView.setOnClickListener { view ->
            val viewGoalIntent = Intent(view.context, ViewGoalActivity::class.java)
            viewGoalIntent.putExtra(IntentKeys.GOAL_OBJECT_KEY.name, this.goals[position])

            view.context.startActivity(viewGoalIntent)
        }
    }
}