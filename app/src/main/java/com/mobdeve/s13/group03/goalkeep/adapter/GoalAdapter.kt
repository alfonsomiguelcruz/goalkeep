package com.mobdeve.s13.group03.goalkeep.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.IntentKeys
import com.mobdeve.s13.group03.goalkeep.MainActivity
import com.mobdeve.s13.group03.goalkeep.ViewGoalActivity
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import com.mobdeve.s13.group03.goalkeep.model.Task
import com.mobdeve.s13.group03.goalkeep.viewholder.GoalViewHolder

class GoalAdapter(private val goals: ArrayList<Goal>, private val activity: Activity) : RecyclerView.Adapter<GoalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val goalViewBinding : GoalListLayoutBinding = GoalListLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return GoalViewHolder(goalViewBinding)
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

    fun addGoalItem(goal : Goal) {
        goals.add(Goal(
            goal.goalId,
            goal.title,
            goal.timeCreated,
            goal.timeExpected,
            goal.timeCompleted,
            goal.description,
            goal.priority,
            goal.state,
            goal.tag
        ))
    }

    fun editGoalItem(goal : Goal) {
//        goals[goal.goalId] = Goal(
//            goal.goalId,
//            goal.title,
//            goal.timeCreated,
//            goal.timeExpected,
//            goal.timeCompleted,
//            goal.description,
//            goal.priority,
//            goal.state,
//            goal.tag
//        )
//
//        notifyItemChanged()
    }

    fun deleteGoalItem(position: Int) {
        val db = GoalKeepDatabase(activity.applicationContext)
        db.deleteGoal(goals[position].goalId)

        goals.removeAt(position)
        notifyItemRemoved(position)
    }
}