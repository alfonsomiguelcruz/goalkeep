package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.AddGoalLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import java.util.Calendar
import java.util.concurrent.Executors

class AddGoalActivity : AppCompatActivity() {
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var dbHandler: GoalKeepDatabase
    private lateinit var addGoalLayoutBinding : AddGoalLayoutBinding

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addGoalLayoutBinding = AddGoalLayoutBinding.inflate(layoutInflater)
        setContentView(addGoalLayoutBinding.root)

        addGoalLayoutBinding.tvAddGoalTimeExpectedDate.text = ""
        addGoalLayoutBinding.tvAddGoalTimeExpectedTime.text = ""

        addGoalLayoutBinding.clAddGoalTimeExpectedDate.setOnClickListener {
            val d = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                addGoalLayoutBinding.tvAddGoalTimeExpectedDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"

                yearInput = year
                monthInput = monthOfYear
                dayInput = dayOfMonth

            },  yearInput, monthInput, dayInput)
            d.show()
        }

        addGoalLayoutBinding.clAddGoalTimeExpectedTime.setOnClickListener {
            val t = TimePickerDialog(this, { view, hourOfDay, minute ->
                addGoalLayoutBinding.tvAddGoalTimeExpectedTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                hourInput = hourOfDay
                minuteInput = minute
            },  hourInput, minuteInput, false)
            t.show()
        }

        addGoalLayoutBinding.btnAddGoal.setOnClickListener {
            executorService.execute {
                dbHandler = GoalKeepDatabase(applicationContext)
                dbHandler.addGoal(
                    Goal(
                        addGoalLayoutBinding.etAddGoalTitle.text.toString(),
                        DateHelper.getCurrentTime(),
                        DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
                        "N/A",
                        addGoalLayoutBinding.etAddGoalDescription.text.toString(),
                        addGoalLayoutBinding.spnGoalpriority.selectedItem.toString(),
                        "Incomplete",
                        addGoalLayoutBinding.etAddGoalTag.text.toString()
                    )
                )

                val i = Intent()
                i.putExtra(IntentKeys.GOAL_OBJECT_KEY.name,
                    Goal(
                        addGoalLayoutBinding.etAddGoalTitle.text.toString(),
                        DateHelper.getCurrentTime(),
                        DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
                        "N/A",
                        addGoalLayoutBinding.etAddGoalDescription.text.toString(),
                        addGoalLayoutBinding.spnGoalpriority.selectedItem.toString(),
                        "Incomplete",
                        addGoalLayoutBinding.etAddGoalTag.text.toString()
                    )
                )

                setResult(ResultCodes.ADD_GOAL.ordinal, i)
                finish()
            }
        }
    }
}