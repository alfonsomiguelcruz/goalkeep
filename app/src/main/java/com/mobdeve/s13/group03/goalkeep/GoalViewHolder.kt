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
        this.goalVB.tvGoalName.text = g.name
        this.goalVB.tvGoalPriority.text = g.priority
        this.goalVB.tvGoalTag.text = g.tag
        this.goalVB.tvGoalTimeExpected.text = g.timeExpected
        this.goalVB.pbGoals.progress = computeProgress(g)

        this.goalVB.llGoalBody.setBackgroundResource(getGoalColor(g.priority))
        this.goalVB.tvGoalPriority.text = "${g.priority} Priority"
        this.goalVB.pbGoals.setBackgroundResource(getGoalSubColor(g.priority))
        this.goalVB.tvGoalTag.background.setTint(getDarkGray())
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

    private fun getGoalColor(priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.drawable.corners_high
            "Medium" -> R.drawable.corners_medium
            "Low" -> R.drawable.corners_low
            else -> {
                R.drawable.corners_gt
            }
        }

        return color
    }

    private fun getGoalSubColor(priority: String): Int {
        val color: Int = when(priority) {
            "High" -> R.drawable.corners_sub_high
            "Medium" -> R.drawable.corners_sub_medium
            "Low" -> R.drawable.corners_sub_low
            else -> {
                R.drawable.corners_gt
            }
        }

        return color
    }

    private fun getDarkGray() : Int {
        return R.color.dark_grey
    }
}