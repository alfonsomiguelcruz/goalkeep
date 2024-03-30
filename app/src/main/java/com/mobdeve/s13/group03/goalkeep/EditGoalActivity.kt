package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.EditGoalDetailsLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import java.util.Calendar
import java.util.concurrent.Executors

class EditGoalActivity : AppCompatActivity() {
    private lateinit var vb : EditGoalDetailsLayoutBinding
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var dbHandler: GoalKeepDatabase

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var goalId : Int = -1

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
            goalId = goal.goalId
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

        vb.etEditGoalTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(vb.etEditGoalTitle.text.toString().isBlank())
                    vb.tvEditGoalTitleError.text = "Please add a valid goal title."
                else
                    vb.tvEditGoalTitleError.text = ""
            }
        })

        vb.etEditGoalDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(vb.etEditGoalDescription.text.toString().isBlank())
                    vb.tvEditGoalDescriptionError.text = "Please add a valid goal description."
                else
                    vb.tvEditGoalDescriptionError.text = ""
            }
        })

        vb.etEditGoalTag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(vb.etEditGoalTag.text.toString().isBlank())
                    vb.tvEditGoalTagError.text = "Please add a valid goal tag."
                else
                    vb.tvEditGoalTagError.text = ""
            }
        })

        vb.ibEditGoalConfirm.setOnClickListener {
            if(isValidInputs(yearInput, monthInput+1, dayInput, hourInput, minuteInput)) {
                executorService.execute {
                    monthInput++
                    dbHandler = GoalKeepDatabase(applicationContext)
                    dbHandler.updateGoal(Goal(
                        goalId,
                        vb.etEditGoalTitle.text.toString(),
                        "NO_EDIT",
                        DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
                        "NO_EDIT",
                        vb.etEditGoalDescription.text.toString(),
                        vb.spnGoalpriority.selectedItem.toString(),
                        "NO_EDIT",
                        vb.etEditGoalTag.text.toString()
                    ))

                    val i = Intent()
                    i.putExtra(IntentKeys.GOAL_OBJECT_KEY.name,
                        Goal(
                            goalId,
                            vb.etEditGoalTitle.text.toString(),
                            "NO_EDIT",
                            DateHelper.getDatabaseTimeFormat(yearInput, monthInput, dayInput, hourInput, minuteInput),
                            "NO_EDIT",
                            vb.etEditGoalDescription.text.toString(),
                            vb.spnGoalpriority.selectedItem.toString(),
                            "NO_EDIT",
                            vb.etEditGoalTag.text.toString()
                        )
                    )

                    setResult(ResultCodes.EDIT_GOAL.ordinal, i)
                    finish()
                }
            } else
                vb.tvEditGoalTimeExpectedError.text = "Please choose a later date."
        }

        vb.ibEditGoalCancel.setOnClickListener {
            this.finish()
        }

        vb.btnDeleteGoal.setOnClickListener {
            dbHandler = GoalKeepDatabase(applicationContext)
            dbHandler.deleteGoal(goalId)

            val i = Intent()
            setResult(ResultCodes.DELETE_GOAL.ordinal, i)
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
        Log.d("MOBDEVE_MCO", "$y-$m-$d $h:$mn")
        Log.d("MOBDEVE_MCO", "$yc-$mc-$dc $hc:$mnc")

        return vb.tvEditGoalTitleError.text.isEmpty() &&
               vb.tvEditGoalDescriptionError.text.isEmpty() &&
               vb.tvEditGoalTagError.text.isEmpty() &&
               DateHelper.isLaterTime(y, m, d, h, mn, yc, mc, dc, hc, mnc)
    }
}