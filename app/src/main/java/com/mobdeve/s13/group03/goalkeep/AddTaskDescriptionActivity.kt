package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddTaskDescriptionLayoutBinding

class AddTaskDescriptionActivity : AppCompatActivity() {
    private var x1 : Float = 0.0F
    private var x2 : Float = 0.0F
    private var y1 : Float = 0.0F
    private var y2 : Float = 0.0F
    private lateinit var addTaskDescriptionLayoutBinding : AddTaskDescriptionLayoutBinding
    companion object {
        const val TITLE_KEY = "TITLE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTaskDescriptionLayoutBinding = AddTaskDescriptionLayoutBinding.inflate(layoutInflater)
        setContentView(addTaskDescriptionLayoutBinding.root)
        addTaskDescriptionLayoutBinding.tvAddTaskDescriptionErr.text = ""

        addTaskDescriptionLayoutBinding.etAddTaskDescription.addTextChangedListener(object:
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty())
                    addTaskDescriptionLayoutBinding.tvAddTaskDescriptionErr.text = getString(R.string.taskdescription_error)
                else
                    addTaskDescriptionLayoutBinding.tvAddTaskDescriptionErr.text = ""
            }

        })
    }

    public override fun onTouchEvent(touchevent : MotionEvent): Boolean {
        val intentAddTime = Intent(this, AddTaskTimeActivity::class.java)
        val taskTitle : String = intent.getStringExtra(AddTaskDescriptionActivity.TITLE_KEY).toString()

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
                    if(addTaskDescriptionLayoutBinding.etAddTaskDescription.text.toString().isNotEmpty()) {
                        addTaskDescriptionLayoutBinding.tvAddTaskDescriptionErr.text = ""

                        intentAddTime.putExtra(AddTaskTimeActivity.TITLE_KEY,
                            taskTitle)

                        intentAddTime.putExtra(AddTaskTimeActivity.DESCRIPTION_KEY,
                            addTaskDescriptionLayoutBinding.etAddTaskDescription.text.toString())

                        startActivity(intentAddTime)
                        finish()
                    } else {
                        addTaskDescriptionLayoutBinding.tvAddTaskDescriptionErr.text = getString(R.string.taskdescription_error)
                    }
                } else {
                    // Go Back to the Previous Pane
                    val intentAddTitle : Intent = Intent(this, AddTaskTitleActivity::class.java)
                    startActivity(intentAddTitle)
                    finish()
                }
            }
        }
        return false
    }
}