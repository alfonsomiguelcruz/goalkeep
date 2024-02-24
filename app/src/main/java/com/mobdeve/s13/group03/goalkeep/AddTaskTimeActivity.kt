package com.mobdeve.s13.group03.goalkeep

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddTaskTimeexpectedLayoutBinding

class AddTaskTimeActivity : AppCompatActivity() {
    private var x1 : Float = 0.0F
    private var x2 : Float = 0.0F
    private var y1 : Float = 0.0F
    private var y2 : Float = 0.0F
    private lateinit var addTaskTimeExpectedLayoutBinding : AddTaskTimeexpectedLayoutBinding

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTaskTimeExpectedLayoutBinding = AddTaskTimeexpectedLayoutBinding.inflate(layoutInflater)
        setContentView(addTaskTimeExpectedLayoutBinding.root)
        addTaskTimeExpectedLayoutBinding.tvAddTaskTimeexpectedErr.text = ""

        val taskTitle : String = intent.getStringExtra(AddGoalTagActivity.TITLE_KEY).toString()
        val taskDescription : String = intent.getStringExtra(AddGoalTagActivity.DESCRIPTION_KEY).toString()
        val taskTime : String = intent.getStringExtra(AddGoalTagActivity.TIME_KEY).toString()
        val intentMain = Intent(this, ViewGoalActivity::class.java)

        addTaskTimeExpectedLayoutBinding.etAddTaskTimeexpected.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty())
                    addTaskTimeExpectedLayoutBinding.tvAddTaskTimeexpectedErr.text = getString(R.string.tasktime_error)
                else
                    addTaskTimeExpectedLayoutBinding.tvAddTaskTimeexpectedErr.text = ""
            }

        })

        addTaskTimeExpectedLayoutBinding.btnAddTask.setOnClickListener {
            if (taskTitle.isNotEmpty() && taskDescription.isNotEmpty()
                && taskTime.isNotEmpty() && addTaskTimeExpectedLayoutBinding.etAddTaskTimeexpected.text.toString()
                    .isNotEmpty()
            ) {
                intentMain.putExtra(MainActivity.TITLE_KEY, taskTitle)
                intentMain.putExtra(MainActivity.DESCRIPTION_KEY, taskDescription)
                intentMain.putExtra(
                    MainActivity.TIME_KEY,
                    addTaskTimeExpectedLayoutBinding.etAddTaskTimeexpected.text.toString()
                )

                startActivity(intentMain)
                finish()
            }
        }
    }

    public override fun onTouchEvent(touchevent : MotionEvent): Boolean {
//        val intentAddTag = Intent(this, AddTaskTimeActivity::class.java)
//        val taskTitle : String = intent.getStringExtra(TITLE_KEY).toString()
//        val taskDescription : String = intent.getStringExtra(DESCRIPTION_KEY).toString()

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
                if(x1 <= x2) {
                    // Go Back to the Previous Pane
                    val intentAddDescription : Intent = Intent(this, AddTaskDescriptionActivity::class.java)
                    startActivity(intentAddDescription)
                    finish()
                }
            }
        }
        return false
    }

}