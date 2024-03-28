package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.EditGoalDetailsLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import java.util.Calendar

class EditGoalActivity : AppCompatActivity() {
    private lateinit var vb : EditGoalDetailsLayoutBinding
    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = EditGoalDetailsLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name, Goal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name)
        }

        if(goal != null) {
            vb.etEditGoalTitle.setText(goal.title)
            vb.tvEditGoalTimeExpectedDate.text = DateHelper.getDateFormat(goal.timeExpected)
            vb.tvEditGoalTimeExpectedTime.text = DateHelper.getTimeFormat(goal.timeExpected)
            vb.spnGoalpriority.prompt = goal.priority
            vb.etEditGoalTag.setText(goal.tag)
            vb.etEditGoalDescription.setText(goal.description)
        }

        // Error Messages
        vb.tvEditGoalTagError.text = ""
        vb.tvEditGoalDescriptionError.text = ""
        vb.tvEditGoalTitleError.text = ""
        vb.tvEditGoalTimeExpectedError.text = ""

        vb.clEditGoalTimeExpectedDate.setOnClickListener {
            val d = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                vb.tvEditGoalTimeExpectedDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"

                yearInput = year
                monthInput = monthOfYear
                dayInput = dayOfMonth

            },  yearInput, monthInput, dayInput)
            d.show()
        }

        vb.clEditGoalTimeExpectedTime.setOnClickListener {
            val t = TimePickerDialog(this, { view, hourOfDay, minute ->
                vb.tvEditGoalTimeExpectedTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                hourInput = hourOfDay
                minuteInput = minute
            },  hourInput, minuteInput, false)
            t.show()
        }

        // TODO: Do Confirm Update Logic
        vb.ibEditGoalConfirm.setOnClickListener {
            this.finish()
        }

        vb.ibEditGoalCancel.setOnClickListener {
            this.finish()
        }

        // TODO: Do Delete Goal Logic
        vb.btnDeleteGoal.setOnClickListener {
            this.finish()
        }
    }
}