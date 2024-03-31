package com.mobdeve.s13.group03.goalkeep.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.DesignClass
import com.mobdeve.s13.group03.goalkeep.database.DatabaseHandler
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import com.mobdeve.s13.group03.goalkeep.model.Task

class GoalViewHolder(private val goalVB: GoalListLayoutBinding) : RecyclerView.ViewHolder(goalVB.root) {

    @SuppressLint("SetTextI18n")
    fun bindGoalData(g: Goal) {
        this.goalVB.tvGoalName.text = g.title
        this.goalVB.tvGoalPriority.text = g.priority
        this.goalVB.tvGoalTag.text = g.tag
        this.goalVB.tvGoalTimeExpected.text = g.timeExpected

        this.goalVB.llGoalBody.setBackgroundResource(DesignClass.getCorneredColor(g.priority))
        this.goalVB.tvGoalPriority.text = "${g.priority} Priority"
        this.goalVB.pbGoals.secondaryProgress = 100
        this.goalVB.tvGoalTag.background.setTint(DesignClass.getDarkGray())
    }

    fun setProgress(progress : Int) {
        this.goalVB.pbGoals.progress = progress
    }
}