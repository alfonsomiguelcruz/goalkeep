package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.AddTaskLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Task
import java.util.Calendar
import java.util.concurrent.Executors

class AddTaskActivity : AppCompatActivity() {
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var dbHandler: GoalKeepDatabase
    private lateinit var addTaskLayoutBinding: AddTaskLayoutBinding

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var goalId : Int = -1
    private lateinit var goalTimeExpected : String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addTaskLayoutBinding = AddTaskLayoutBinding.inflate(layoutInflater)
        setContentView(addTaskLayoutBinding.root)

        addTaskLayoutBinding.clAddTaskTimeExpectedDate.setOnClickListener {
            val d = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                addTaskLayoutBinding.tvAddTaskTimeExpectedDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"

                yearInput = year
                monthInput = monthOfYear
                dayInput = dayOfMonth

            },  yearInput, monthInput, dayInput)
            d.show()
        }

        addTaskLayoutBinding.clAddTaskTimeExpectedTime.setOnClickListener {
            val t = TimePickerDialog(this, { view, hourOfDay, minute ->
                addTaskLayoutBinding.tvAddTaskTimeExpectedTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                hourInput = hourOfDay
                minuteInput = minute
            },  hourInput, minuteInput, false)
            t.show()
        }

        val intent = intent
        goalId = intent.getIntExtra(IntentKeys.TASK_GOAL_ID_KEY.name, -1)
        goalTimeExpected = intent.getStringExtra(IntentKeys.GOAL_TIME_EXPECTED_KEY.name).toString()

        addTaskLayoutBinding.tvAddTaskTimeExpectedDate.text = ""
        addTaskLayoutBinding.tvAddTaskTimeExpectedTime.text = ""

        addTaskLayoutBinding.tvAddTaskTitleError.text = ""
        addTaskLayoutBinding.tvAddTaskDescriptionError.text = ""
        addTaskLayoutBinding.tvAddTaskTimeExpectedError.text = ""

        addTaskLayoutBinding.etAddTaskTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(addTaskLayoutBinding.etAddTaskTitle.text.toString().isBlank())
                    addTaskLayoutBinding.tvAddTaskTitleError.text = "Please add a valid task title."
                else
                    addTaskLayoutBinding.tvAddTaskTitleError.text = ""
            }
        })

        addTaskLayoutBinding.etAddTaskDescription.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(addTaskLayoutBinding.etAddTaskDescription.text.toString().isBlank())
                    addTaskLayoutBinding.tvAddTaskDescriptionError.text = "Please add a valid task description."
                else
                    addTaskLayoutBinding.tvAddTaskDescriptionError.text = ""
            }
        })

        addTaskLayoutBinding.btnAddTask.setOnClickListener {
            if(isValidInputs(yearInput, monthInput+1, dayInput, hourInput, minuteInput)) {
                executorService.execute {
                    monthInput++
                    dbHandler = GoalKeepDatabase(applicationContext)
                    val id = dbHandler.addTask(
                        Task(
                            addTaskLayoutBinding.etAddTaskTitle.text.toString(),
                            DateHelper.getCurrentTime(),
                            DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
                            "N/A",
                            addTaskLayoutBinding.etAddTaskDescription.text.toString(),
                            addTaskLayoutBinding.spnTaskpriority.selectedItem.toString(),
                            "Incomplete",
                            goalId
                        ), goalId
                    )

                    val i = Intent()
                    i.putExtra(IntentKeys.TASK_OBJECT_KEY.name,
                        Task(
                            id.toInt(),
                            addTaskLayoutBinding.etAddTaskTitle.text.toString(),
                            DateHelper.getCurrentTime(),
                            DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
                            "N/A",
                            addTaskLayoutBinding.etAddTaskDescription.text.toString(),
                            addTaskLayoutBinding.spnTaskpriority.selectedItem.toString(),
                            "Incomplete",
                            goalId
                        )
                    )
                    i.putExtra(IntentKeys.TASK_GOAL_ID_KEY.name, goalId)

                    setResult(ResultCodes.ADD_TASK.ordinal, i)
                    finish()
                }
            } else {
                addTaskLayoutBinding.tvAddTaskTimeExpectedError.text = "Please make the date and time set before the goal deadline."
            }
        }
    }

    private fun isValidInputs(y : Int, m : Int, d : Int, h : Int, mn : Int) : Boolean {
        val currentDate = DateHelper.getCurrentTime()
        val yc = Integer.parseInt(currentDate.substring(0,4))
        val mc = Integer.parseInt(currentDate.substring(5,7))
        val dc = Integer.parseInt(currentDate.substring(8,10))
        val hc = Integer.parseInt(currentDate.substring(11,13))
        val mnc = Integer.parseInt(currentDate.substring(14,16))

        val yg = Integer.parseInt(goalTimeExpected.substring(0,4))
        val mg = Integer.parseInt(goalTimeExpected.substring(5,7))
        val dg = Integer.parseInt(goalTimeExpected.substring(8,10))
        val hg = Integer.parseInt(goalTimeExpected.substring(11,13))
        val mng = Integer.parseInt(goalTimeExpected.substring(14,16))

        Log.d("MOBDEVE_MCO", goalTimeExpected)
        Log.d("MOBDEVE_MCO", currentDate)

        return addTaskLayoutBinding.tvAddTaskTitleError.text.isEmpty() &&
               addTaskLayoutBinding.tvAddTaskDescriptionError.text.isEmpty() &&
               DateHelper.isLaterTime(yg, mg, dg, hg, mng, y, m, d, h, mn) &&
               DateHelper.isLaterTime(y, m, d, h, mn, yc, mc, dc, hc, mnc)
    }
}