package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddGoalTimeexpectedLayoutBinding

class AddGoalTimeActivity : AppCompatActivity() {
    private var x1 : Float = 0.0F
    private var x2 : Float = 0.0F
    private var y1 : Float = 0.0F
    private var y2 : Float = 0.0F
    private lateinit var addGoalTimeExpectedLayoutBinding : AddGoalTimeexpectedLayoutBinding

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addGoalTimeExpectedLayoutBinding = AddGoalTimeexpectedLayoutBinding.inflate(layoutInflater)
        setContentView(addGoalTimeExpectedLayoutBinding.root)
        addGoalTimeExpectedLayoutBinding.tvAddGoalTimeexpectedErr.text = ""

        addGoalTimeExpectedLayoutBinding.etAddGoalTimeexpected.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty())
                    addGoalTimeExpectedLayoutBinding.tvAddGoalTimeexpectedErr.text = getString(R.string.goaltime_error)
                else
                    addGoalTimeExpectedLayoutBinding.tvAddGoalTimeexpectedErr.text = ""
            }

        })
    }

    public override fun onTouchEvent(touchevent : MotionEvent): Boolean {
        val intentAddTag = Intent(this, AddGoalTagActivity::class.java)
        val goalTitle : String = intent.getStringExtra(AddGoalTimeActivity.TITLE_KEY).toString()
        val goalDescription : String = intent.getStringExtra(AddGoalTimeActivity.DESCRIPTION_KEY).toString()

        when(touchevent.action) {
            MotionEvent.ACTION_DOWN -> {
                print("x1")
                x1 = touchevent.x
                y1 = touchevent.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = touchevent.x
                y2 = touchevent.y

                // Swipe Left, else Swipe Right
                if(x1 >= x2) {
                    // Carry Over Data to the Next Activity
                    // Title -> Description -> [Time -> Tag]
                    if(addGoalTimeExpectedLayoutBinding.tvAddGoalTimeexpectedErr.text.toString().isNotEmpty()) {
                        addGoalTimeExpectedLayoutBinding.tvAddGoalTimeexpectedErr.text = ""

                        intentAddTag.putExtra(AddGoalTagActivity.TITLE_KEY,
                            goalTitle)

                        intentAddTag.putExtra(AddGoalTagActivity.DESCRIPTION_KEY,
                            goalDescription)

                        intentAddTag.putExtra(AddGoalTagActivity.TIME_KEY,
                            addGoalTimeExpectedLayoutBinding.etAddGoalTimeexpected.text.toString())

                        startActivity(intentAddTag)
                        finish()
                    } else {
                        addGoalTimeExpectedLayoutBinding.tvAddGoalTimeexpectedErr.text = getString(R.string.goaltime_error)
                    }

                } else {
                    // Go Back to the Previous Pane
                    val intentAddDescription : Intent = Intent(this, AddGoalDescriptionActivity::class.java)
                    startActivity(intentAddDescription)
                    finish()
                }
            }
        }
        return false
    }

}