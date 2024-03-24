package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding

class GoalViewHolder(private val goalVB: GoalListLayoutBinding, private val tasksList: ArrayList<Task>) : RecyclerView.ViewHolder(goalVB.root) {

    @SuppressLint("SetTextI18n")
    fun bindGoalData(g: Goal) {
        this.goalVB.tvGoalName.text = g.title
        this.goalVB.tvGoalPriority.text = g.priority
        this.goalVB.tvGoalTag.text = g.tag
        this.goalVB.tvGoalTimeExpected.text = g.timeExpected
        this.goalVB.pbGoals.progress = computeProgress(g)

        this.goalVB.llGoalBody.setBackgroundResource(DesignClass.getCorneredColor(g.priority))
        this.goalVB.tvGoalPriority.text = "${g.priority} Priority"
        this.goalVB.pbGoals.setBackgroundResource(DesignClass.getSubColor(g.priority))
        this.goalVB.tvGoalTag.background.setTint(DesignClass.getDarkGray())
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
            progress = 0
        }



        return progress
    }
}