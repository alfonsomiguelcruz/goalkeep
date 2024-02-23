package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddGoalTagnameLayoutBinding

class AddGoalTagActivity : AppCompatActivity() {
    private var x1 : Float = 0.0F
    private var x2 : Float = 0.0F
    private var y1 : Float = 0.0F
    private var y2 : Float = 0.0F
    private lateinit var addGoalTagnameLayoutBinding : AddGoalTagnameLayoutBinding

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
        const val TIME_KEY = "TIME_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addGoalTagnameLayoutBinding = AddGoalTagnameLayoutBinding.inflate(layoutInflater)
        setContentView(addGoalTagnameLayoutBinding.root)

        val goalTitle : String = intent.getStringExtra(AddGoalTagActivity.TITLE_KEY).toString()
        val goalDescription : String = intent.getStringExtra(AddGoalTagActivity.DESCRIPTION_KEY).toString()
        val goalTime : String = intent.getStringExtra(AddGoalTagActivity.TIME_KEY).toString()
        val intentMain = Intent(this, MainActivity::class.java)

        addGoalTagnameLayoutBinding.btnAddGoal.setOnClickListener(View.OnClickListener {
            if(goalTitle.isNotEmpty() && goalDescription.isNotEmpty()
                && goalTime.isNotEmpty() && addGoalTagnameLayoutBinding.etAddGoalTagname.text.toString().isNotEmpty()) {
                startActivity(intentMain)
                finish()
            }
        })
    }

    public override fun onTouchEvent(touchevent : MotionEvent): Boolean {
        val intentAddTag = Intent(this, MainActivity::class.java)
        val goalTitle : String = intent.getStringExtra(AddGoalTagActivity.TITLE_KEY).toString()
        val goalDescription : String = intent.getStringExtra(AddGoalTagActivity.DESCRIPTION_KEY).toString()
        val goalTime : String = intent.getStringExtra(AddGoalTagActivity.TIME_KEY).toString()

        when(touchevent.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = touchevent.x
                y1 = touchevent.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = touchevent.x
                y2 = touchevent.y

                // Swipe Left, else Swipe Right
                if(x1 >= x2) {
                    // Carry Over Data to the Next Activity
                    // Title -> Description -> Time -> Tag

                    intentAddTag.putExtra(MainActivity.TITLE_KEY,
                        goalTitle)

                    intentAddTag.putExtra(MainActivity.DESCRIPTION_KEY,
                        goalDescription)

                    intentAddTag.putExtra(MainActivity.TIME_KEY,
                        goalTime)

                    intentAddTag.putExtra(MainActivity.TAG_KEY,
                        addGoalTagnameLayoutBinding.etAddGoalTagname.text.toString())

                    startActivity(intentAddTag)
                    finish()
                } else {
                    // Go Back to the Previous Pane
                    val intentAddTime : Intent = Intent(this, AddGoalTimeActivity::class.java)
                    startActivity(intentAddTime)
                    finish()
                }
            }
        }
        return false
    }
}