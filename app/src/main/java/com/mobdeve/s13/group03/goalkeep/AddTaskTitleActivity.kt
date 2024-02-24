package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddTaskTitleLayoutBinding

class AddTaskTitleActivity: AppCompatActivity() {
    private var x1 : Float = 0.0f
    private var x2 : Float = 0.0f
    private var y1 : Float = 0.0f
    private var y2 : Float = 0.0f
    private lateinit var taskTitleLayoutBinding : AddTaskTitleLayoutBinding

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskTitleLayoutBinding = AddTaskTitleLayoutBinding.inflate(layoutInflater)
        setContentView(taskTitleLayoutBinding.root)
        taskTitleLayoutBinding.tvAddTaskTitleErr.text = ""

        taskTitleLayoutBinding.etAddTaskTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty())
                    taskTitleLayoutBinding.tvAddTaskTitleErr.text = getString(R.string.goaltitle_error)
                else
                    taskTitleLayoutBinding.tvAddTaskTitleErr.text = ""
            }
        })

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
                    if(taskTitleLayoutBinding.tvAddTaskTitleErr.text.toString().isNotEmpty()) {
                        taskTitleLayoutBinding.tvAddTaskTitleErr.text = ""

                        val intentAddDescription = Intent(this, AddTaskDescriptionActivity::class.java)
                        intentAddDescription.putExtra(AddTaskDescriptionActivity.TITLE_KEY,
                            taskTitleLayoutBinding.etAddTaskTitle.text.toString())
                        startActivity(intentAddDescription)
                    } else {
                        taskTitleLayoutBinding.tvAddTaskTitleErr.text = getString(R.string.goaltitle_error)
                    }
                }
            }
            else -> {
                println("Error")
            }
        }
        return false
    }
}