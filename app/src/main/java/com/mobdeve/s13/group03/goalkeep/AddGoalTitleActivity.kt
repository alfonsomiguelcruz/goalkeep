package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddGoalTitleLayoutBinding
import com.mobdeve.s13.group03.goalkeep.databinding.GoalListLayoutBinding

class AddGoalTitleActivity : AppCompatActivity() {
    private var x1 : Float = 0.0f
    private var x2 : Float = 0.0f
    private var y1 : Float = 0.0f
    private var y2 : Float = 0.0f
    private lateinit var goalTitleLayoutBinding : AddGoalTitleLayoutBinding

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalTitleLayoutBinding = AddGoalTitleLayoutBinding.inflate(layoutInflater)
        setContentView(goalTitleLayoutBinding.root)
        goalTitleLayoutBinding.tvAddGoalTitleErr.text = ""
    }

    override fun onTouchEvent(touchevent : MotionEvent): Boolean {
        println("TOUCHED EVENT")
        when(touchevent.action) {
            MotionEvent.ACTION_DOWN -> {
                print("ACTION DOWN")
                x1 = touchevent.x
                y1 = touchevent.y
            }
            MotionEvent.ACTION_UP -> {
                print("ACTION UP")
                x2 = touchevent.x
                y2 = touchevent.y

                // Swipe Left
                if(x1 >= x2) {
                    // [Title -> Description] -> Time -> Tag
                    val intentAddDescription = Intent(this, AddGoalDescriptionActivity::class.java)
                    intentAddDescription.putExtra(AddGoalDescriptionActivity.TITLE_KEY,
                        goalTitleLayoutBinding.etAddGoalTitle.text.toString())
                    startActivity(intentAddDescription)
                }
            }
            else -> {
                println("Error")
            }
        }
        return false
    }
}