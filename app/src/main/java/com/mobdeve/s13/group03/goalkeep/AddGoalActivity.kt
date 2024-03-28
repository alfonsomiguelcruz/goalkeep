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
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addGoalLayoutBinding = AddGoalLayoutBinding.inflate(layoutInflater)
        setContentView(addGoalLayoutBinding.root)

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

        addGoalLayoutBinding.tvAddGoalTimeExpectedDate.text = ""
        addGoalLayoutBinding.tvAddGoalTimeExpectedTime.text = ""

        addGoalLayoutBinding.tvAddGoalTagError.text = ""
        addGoalLayoutBinding.tvAddGoalDescriptionError.text = ""
        addGoalLayoutBinding.tvAddGoalTitleError.text = ""
        addGoalLayoutBinding.tvAddGoalTimeExpectedError

        addGoalLayoutBinding.etAddGoalTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(addGoalLayoutBinding.etAddGoalTitle.text.toString().isBlank())
                    addGoalLayoutBinding.tvAddGoalTitleError.text = "Please add a valid goal title."
                else
                    addGoalLayoutBinding.tvAddGoalTitleError.text = ""
            }
        })

        addGoalLayoutBinding.etAddGoalDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(addGoalLayoutBinding.etAddGoalDescription.text.toString().isBlank())
                    addGoalLayoutBinding.tvAddGoalDescriptionError.text = "Please add a valid goal description."
                else
                    addGoalLayoutBinding.tvAddGoalDescriptionError.text = ""
            }
        })

        addGoalLayoutBinding.etAddGoalTag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(addGoalLayoutBinding.etAddGoalTag.text.toString().isBlank())
                    addGoalLayoutBinding.tvAddGoalTagError.text = "Please add a valid goal tag."
                else
                    addGoalLayoutBinding.tvAddGoalTagError.text = ""
            }
        })

        addGoalLayoutBinding.btnAddGoal.setOnClickListener {
            if(isValidInputs(yearInput, monthInput, dayInput, hourInput, minuteInput)) {
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
            } else {
                addGoalLayoutBinding.tvAddGoalTimeExpectedError.text = "Please choose a later date and time."
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
        Log.d("MOBDEVE_MCO", "$y-$m-$d $h:$mn")
        Log.d("MOBDEVE_MCO", "$yc-$mc-$dc $hc:$mnc")

        return addGoalLayoutBinding.tvAddGoalTitleError.text.isEmpty() &&
                addGoalLayoutBinding.tvAddGoalDescriptionError.text.isEmpty() &&
                addGoalLayoutBinding.tvAddGoalTagError.text.isEmpty() &&
                DateHelper.isLaterTime(y, m, d, h, mn, yc, mc, dc, hc, mnc)
    }
}