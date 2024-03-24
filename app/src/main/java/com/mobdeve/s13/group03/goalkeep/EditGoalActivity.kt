package com.mobdeve.s13.group03.goalkeep

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.EditGoalDetailsLayoutBinding

class EditGoalActivity : AppCompatActivity() {
    private lateinit var vb : EditGoalDetailsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = EditGoalDetailsLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name, Goal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name)
        }

        if(goal != null) {
            vb.etEditGoalTitle.setText(goal.title)
            vb.etEditGoalTimeExpected.setText(goal.timeExpected)
            vb.etEditGoalPriority.setText(goal.priority)
            vb.etEditGoalTag.setText(goal.tag)
            vb.etEditGoalDescription.setText(goal.description)
        }

        // TODO: Do Confirm Logic
        vb.ibEditGoalConfirm.setOnClickListener {
            this.finish()
        }

        // TODO: Do Cancel Logic
        vb.ibEditGoalCancel.setOnClickListener {
            this.finish()
        }

        // TODO: Do Delete Logic
        vb.btnDeleteGoal.setOnClickListener {
            this.finish()
        }
    }
}