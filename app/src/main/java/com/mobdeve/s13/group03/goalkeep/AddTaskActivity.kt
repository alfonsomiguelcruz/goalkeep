package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddTaskLayoutBinding
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {
    private lateinit var addTaskLayoutBinding: AddTaskLayoutBinding

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

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
    }
}