package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.EditTaskDetailsLayoutBinding
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {
    private lateinit var editTaskDetailsLayoutBinding: EditTaskDetailsLayoutBinding
    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

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
                editTaskDetailsLayoutBinding.tvEditTaskTimeExpectedTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getMinuteFormat(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                hourInput = hourOfDay
                minuteInput = minute
            },  hourInput, minuteInput, false)
            t.show()
        }

        // TODO: Do Confirm Logic
        editTaskDetailsLayoutBinding.ibEditTaskConfirm.setOnClickListener {
            this.finish()
        }

        editTaskDetailsLayoutBinding.ibEditTaskCancel.setOnClickListener {
            this.finish()
        }

        // TODO: Do Delete Logic
        editTaskDetailsLayoutBinding.btnDeleteTask.setOnClickListener {
            this.finish()
        }
    }
}