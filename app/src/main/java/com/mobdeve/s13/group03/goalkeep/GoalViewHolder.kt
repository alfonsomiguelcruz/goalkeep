package com.mobdeve.s13.group03.goalkeep

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding

class GoalViewHolder(private val goalVB: GoalListLayoutBinding, private val tasksList: ArrayList<Task>) : RecyclerView.ViewHolder(goalVB.root) {
    fun bindGoalData(g: Goal) {
        this.goalVB.tvGoalName.text = g.name
        this.goalVB.tvGoalPriority.text = g.priority
        this.goalVB.tvGoalTag.text = g.tag
        this.goalVB.tvGoalTimeExpected.text = g.timeExpected
        this.goalVB.pbGoals.progress = computeProgress(g)
    }

    private fun computeProgress(g: Goal): Int {
        var totalMatched: Int = 0
        var totalComplete: Int = 0

        for(i in 0..<tasksList.size)
            if(tasksList[i].goalId == g.goalId) {
                totalMatched++
                if (tasksList[i].state == "Complete")
                    totalComplete++
            }

        return (totalComplete * 100 / totalMatched)
    }
}