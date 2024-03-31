package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.EditTaskDetailsLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Task
import java.util.Calendar
import java.util.concurrent.Executors

class EditTaskActivity : AppCompatActivity() {
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var dbHandler: GoalKeepDatabase

    private lateinit var editTaskDetailsLayoutBinding: EditTaskDetailsLayoutBinding
    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var taskId = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editTaskDetailsLayoutBinding = EditTaskDetailsLayoutBinding.inflate(layoutInflater)
        setContentView(editTaskDetailsLayoutBinding.root)

        val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(IntentKeys.TASK_OBJECT_KEY.name, Task::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(IntentKeys.TASK_OBJECT_KEY.name)
        }

        if (task != null) {
            editTaskDetailsLayoutBinding.etEditTaskTitle.setText(task.name)
            editTaskDetailsLayoutBinding.etEditTaskDescription.setText(task.description)
            editTaskDetailsLayoutBinding.spnTaskpriority.prompt = task.priority
            editTaskDetailsLayoutBinding.spnTaskstate.prompt = task.state
            editTaskDetailsLayoutBinding.tvEditTaskTimeExpectedDate.text = DateHelper.getDateFormat(task.timeExpected)
            editTaskDetailsLayoutBinding.tvEditTaskTimeExpectedTime.text = DateHelper.getTimeFormat(task.timeExpected)
            taskId = task.taskId
        }

        // Error Messages
        editTaskDetailsLayoutBinding.tvEditTaskTitleError.text = ""
        editTaskDetailsLayoutBinding.tvEditTaskDescriptionError.text = ""
        editTaskDetailsLayoutBinding.tvEditTaskTimeExpectedError.text = ""


        editTaskDetailsLayoutBinding.clEditTaskTimeExpectedDate.setOnClickListener {
            val d = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                editTaskDetailsLayoutBinding.tvEditTaskTimeExpectedDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                yearInput = year
                monthInput = monthOfYear
                dayInput = dayOfMonth
            },  yearInput, monthInput, dayInput)
            d.show()
        }

        editTaskDetailsLayoutBinding.clEditTaskTimeExpectedTime.setOnClickListener {
            val t = TimePickerDialog(this, { view, hourOfDay, minute ->
                editTaskDetailsLayoutBinding.tvEditTaskTimeExpectedTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                hourInput = hourOfDay
                minuteInput = minute
            },  hourInput, minuteInput, false)
            t.show()
        }

        editTaskDetailsLayoutBinding.ibEditTaskConfirm.setOnClickListener {
            if(isValidInputs(yearInput, monthInput+1, dayInput, hourInput, minuteInput)) {
                executorService.execute {
                    monthInput++
                    dbHandler = GoalKeepDatabase(applicationContext)
                    var date = "NO_EDIT"

                    if(editTaskDetailsLayoutBinding.spnTaskstate.selectedItem.toString().equals("Complete"))
                        date = DateHelper.getCurrentTime()


                    dbHandler.updateTask(Task (
                        taskId,
                        editTaskDetailsLayoutBinding.etEditTaskTitle.text.toString(),
                        "NO_EDIT",
                        DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
                        date,
                        editTaskDetailsLayoutBinding.etEditTaskDescription.text.toString(),
                        editTaskDetailsLayoutBinding.spnTaskpriority.selectedItem.toString(),
                        editTaskDetailsLayoutBinding.spnTaskstate.selectedItem.toString(),
                        -1
                    ))

//                    val i = Intent(this, EditGoalActivity::class.java)
//                    i.putExtra(IntentKeys.TASK_OBJECT_KEY.name,
//                        Task (
//                            taskId,
//                            editTaskDetailsLayoutBinding.etEditTaskTitle.text.toString(),
//                            "NO_EDIT",
//                            DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
//                            date,
//                            editTaskDetailsLayoutBinding.etEditTaskDescription.text.toString(),
//                            editTaskDetailsLayoutBinding.spnTaskpriority.selectedItem.toString(),
//                            editTaskDetailsLayoutBinding.spnTaskstate.selectedItem.toString(),
//                            -1
//                        )
//                    )
//
//                    startActivity(i)
                    finish()
                }
            }
        }

        editTaskDetailsLayoutBinding.ibEditTaskCancel.setOnClickListener {
            this.finish()
        }

        editTaskDetailsLayoutBinding.btnDeleteTask.setOnClickListener {
            dbHandler = GoalKeepDatabase(applicationContext)
            dbHandler.deleteTask(taskId)

//            val i = Intent(this, EditGoalActivity::class.java)
//            startActivity(i)
            finish()
        }
    }

    private fun isValidInputs(y : Int, m : Int, d : Int, h : Int, mn : Int) : Boolean {
        val currentDate = DateHelper.getCurrentTime()
        val yc = Integer.parseInt(currentDate.substring(0,4))
        val mc = Integer.parseInt(currentDate.substring(5,7))
        val dc = Integer.parseInt(currentDate.substring(8,10))
        val hc = Integer.parseInt(currentDate.substring(11,13))
        val mnc = Integer.parseInt(currentDate.substring(14,16))

        return editTaskDetailsLayoutBinding.tvEditTaskTitleError.text.isEmpty() &&
               editTaskDetailsLayoutBinding.tvEditTaskDescriptionError.text.isEmpty() &&
               DateHelper.isLaterTime(y, m, d, h, mn, yc, mc, dc, hc, mnc)
    }
}