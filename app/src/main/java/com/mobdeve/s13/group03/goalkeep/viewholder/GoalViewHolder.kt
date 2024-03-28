package com.mobdeve.s13.group03.goalkeep.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.DesignClass
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
        //TODO
        this.goalVB.pbGoals.progress = 10

        this.goalVB.llGoalBody.setBackgroundResource(DesignClass.getCorneredColor(g.priority))
        this.goalVB.tvGoalPriority.text = "${g.priority} Priority"
        this.goalVB.pbGoals.setBackgroundResource(DesignClass.getSubColor(g.priority))
        this.goalVB.tvGoalTag.background.setTint(DesignClass.getDarkGray())
    }

//    private fun computeProgress(g: Goal): Int {
//        var totalMatched: Int = 0
//        var totalComplete: Int = 0
//        var progress: Int
//
//        for(i in 0..<tasksList.size)
//            if(tasksList[i].goalId == g.goalId) {
//                totalMatched++
//                if (tasksList[i].state == "Complete")
//                    totalComplete++
//            }
//
//        try {
//            ((totalComplete * 100) / totalMatched).also { progress = it }
//        } catch (e: ArithmeticException) {
//            progress = 0
//        }
//
//
//
//        return progress
//    }
}