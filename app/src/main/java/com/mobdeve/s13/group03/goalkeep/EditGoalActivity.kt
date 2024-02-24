package com.mobdeve.s13.group03.goalkeep

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.EditGoalDetailsLayoutBinding

class EditGoalActivity : AppCompatActivity() {
    private lateinit var goal: Goal
    private lateinit var vb : EditGoalDetailsLayoutBinding

    companion object {
        const val GOAL_KEY: String = "GOAL_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = EditGoalDetailsLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

//        goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ({
//            intent.getParcelableExtra(ViewGoalActivity.GOAL_KEY, Goal::class.java)
//        })!! else ({
//            @Suppress("DEPRECATION")
//            intent.getParcelableExtra(ViewGoalActivity.GOAL_KEY)
//        })!!
//
//        vb.etEditGoalTitle.text = goal.name
//        vb.etEditGoalTimeExpected.text = goal.timeExpected
//        vb.etEditGoalPriority.text = goal.priority
//        vb.etEditGoalTag.text = goal.tag
//        vb.etEditGoalDescription.text = goal.description

    }
}