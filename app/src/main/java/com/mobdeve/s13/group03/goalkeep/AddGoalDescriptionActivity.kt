package com.mobdeve.s13.group03.goalkeep

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddGoalDescriptionLayoutBinding

class AddGoalDescriptionActivity : AppCompatActivity() {
    private var x1 : Float = 0.0F
    private var x2 : Float = 0.0F
    private var y1 : Float = 0.0F
    private var y2 : Float = 0.0F
    private lateinit var addGoalDescriptionLayoutBinding : AddGoalDescriptionLayoutBinding
    companion object {
        const val TITLE_KEY = "TITLE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addGoalDescriptionLayoutBinding = AddGoalDescriptionLayoutBinding.inflate(layoutInflater)
        setContentView(addGoalDescriptionLayoutBinding.root)
        addGoalDescriptionLayoutBinding.tvAddGoalDescriptionErr.text = ""

        addGoalDescriptionLayoutBinding.etAddGoalDescription.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty())
                    addGoalDescriptionLayoutBinding.tvAddGoalDescriptionErr.text = getString(R.string.goaldescription_error)
                else
                    addGoalDescriptionLayoutBinding.tvAddGoalDescriptionErr.text = ""
            }

        })
    }

    public override fun onTouchEvent(touchevent : MotionEvent): Boolean {
        val intentAddTime = Intent(this, AddGoalTimeActivity::class.java)
        val goalTitle : String = intent.getStringExtra(AddGoalDescriptionActivity.TITLE_KEY).toString()

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
                    // Title -> [Description -> Time] -> Tag
                    if(addGoalDescriptionLayoutBinding.etAddGoalDescription.text.toString().isNotEmpty()) {
                        addGoalDescriptionLayoutBinding.tvAddGoalDescriptionErr.text = ""

                        intentAddTime.putExtra(AddGoalTimeActivity.TITLE_KEY,
                            goalTitle)

                        intentAddTime.putExtra(AddGoalTimeActivity.DESCRIPTION_KEY,
                            addGoalDescriptionLayoutBinding.etAddGoalDescription.text.toString())

                        startActivity(intentAddTime)
                        finish()
                    } else {
                        addGoalDescriptionLayoutBinding.tvAddGoalDescriptionErr.text = getString(R.string.goaldescription_error)
                    }
                } else {
                    // Go Back to the Previous Pane
                    val intentAddTitle : Intent = Intent(this, AddGoalTitleActivity::class.java)
                    startActivity(intentAddTitle)
                    finish()
                }
            }
        }
        return false
    }
}