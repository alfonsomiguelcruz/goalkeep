package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding

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
        holder.itemView.setOnClickListener {
            println("Clicked Goal " + this.goals[position].name)
            Toast.makeText(
                holder.itemView.context,
                "Goal: " + this.goals[position].name,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}