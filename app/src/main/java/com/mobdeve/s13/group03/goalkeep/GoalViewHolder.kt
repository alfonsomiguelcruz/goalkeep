package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding

class GoalViewHolder(private val goalVB: GoalListLayoutBinding, private val tasksList: ArrayList<Task>) : RecyclerView.ViewHolder(goalVB.root) {
    @SuppressLint("ResourceAsColor")
    fun bindGoalData(g: Goal) {
        this.goalVB.tvGoalName.text = g.name
        this.goalVB.tvGoalPriority.text = g.priority
        this.goalVB.tvGoalTag.text = g.tag
        this.goalVB.tvGoalTimeExpected.text = g.timeExpected
        this.goalVB.pbGoals.progress = computeProgress(g)

//        this.goalVB.llGoalBody.setBackground()
        this.goalVB.llGoalBody.background.setTint(getGoalColor(g.priority))
//        this.goalVB.pbGoals.background.setTint(getGoalSubColor(g.priority))
//        this.goalVB.tvGoalPriority.setTextColor(getGoalSubColor(g.priority))
//        this.goalVB.tvGoalPriority.setBackgroundColor(R.color.white)
//        this.goalVB.tvGoalTag.background.setTint(R.color.black)
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

    private fun getGoalColor(priority: String): Int {
        var color: Int = 0

        when(priority) {
            "High" -> color = R.color.high_default
            "Medium" -> color = R.color.medium_default
            "Low" -> color = R.color.low_default
        }

        return color
    }

    private fun getGoalSubColor(priority: String): Int {
        var subcolor: Int = 0

        when(priority) {
            "High" -> subcolor = R.color.high_sub
            "Medium" -> subcolor = R.color.medium_sub
            "Low" -> subcolor = R.color.low_sub
        }

        return subcolor
    }
}