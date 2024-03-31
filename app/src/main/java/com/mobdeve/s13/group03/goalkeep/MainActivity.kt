package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.adapter.GoalAdapter
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.ActivityMainBinding
import com.mobdeve.s13.group03.goalkeep.databinding.GoalSortFilterDialogLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import com.mobdeve.s13.group03.goalkeep.model.Task
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var vb : ActivityMainBinding
    private lateinit var rv : RecyclerView
    private lateinit var goalsAdapter : GoalAdapter
    private lateinit var goalsTouchHelper: ItemTouchHelper

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private val addGoalResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.data != null) {
                if(result.resultCode == ResultCodes.ADD_GOAL.ordinal) {
                    val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data!!.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name, Goal::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        result.data!!.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name)
                    }

                    if (goal != null) {
                        this.goalsAdapter.addGoalItem(goal)

                        vb.rvGoals.visibility = View.VISIBLE
                        vb.tvNogoalMsg.visibility = View.GONE

                        this.goalsAdapter.notifyItemInserted(this.goalsAdapter.itemCount - 1)
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.fabAddGoalList.setOnClickListener {
            val addGoalIntent = Intent(this, AddGoalActivity::class.java)
            addGoalResultLauncher.launch(addGoalIntent)
        }

        vb.etGoalSearchbar.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                vb.ibGoalFilter.visibility = View.GONE
                vb.ibSettings.visibility = View.GONE
                vb.ibAlarms.visibility = View.GONE
                vb.ibCompletedgoals.visibility = View.GONE
            } else {
                vb.ibGoalFilter.visibility = View.VISIBLE
                vb.ibSettings.visibility = View.VISIBLE
                vb.ibAlarms.visibility = View.VISIBLE
                vb.ibCompletedgoals.visibility = View.VISIBLE
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }

        vb.etGoalSearchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                goalsAdapter.clear()
                goalsAdapter = GoalAdapter(GoalKeepDatabase(applicationContext).getSearchQuery(s.toString()), this@MainActivity)
                rv.adapter = goalsAdapter
            }

        })

        // Connecting Recycler View with Adapter
        var db = GoalKeepDatabase(applicationContext)
        this.rv = vb.rvGoals
        this.goalsAdapter = GoalAdapter(db.getGoals(), this)
        this.rv.adapter = this.goalsAdapter

        // Orientation of the Activity Layout
        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL
        vb.rvGoals.layoutManager = verticalLM

        // Swipe Callback Function for the Items
        val goalSwipeCallback =
            GoalSwipeCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, this)
        goalSwipeCallback.goalAdapter = this.goalsAdapter
        goalsTouchHelper = ItemTouchHelper(goalSwipeCallback)
        goalsTouchHelper.attachToRecyclerView(this.rv)

        if (this.goalsAdapter.itemCount == 0) {
            vb.rvGoals.visibility = View.GONE
            vb.tvNogoalMsg.visibility = View.VISIBLE
        } else {
            vb.rvGoals.visibility = View.VISIBLE
            vb.tvNogoalMsg.visibility = View.GONE
        }

        vb.ibGoalFilter.setOnClickListener {
            val d = Dialog(this)
            val vb = GoalSortFilterDialogLayoutBinding.inflate(layoutInflater)

            d.setContentView(vb.root)

            vb.tvGoalFilterTimeExpectedStartDate.text = ""
            vb.tvGoalFilterTimeExpectedEndDate.text = ""
            vb.tvGoalFilterTimeExpectedStartTime.text = ""
            vb.tvGoalFilterTimeExpectedEndTime.text = ""

            // PRIORITY
            vb.tvGoalFilterPriorityTabHigh.setOnClickListener {
                if(vb.tvGoalFilterPriorityTabHigh.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_selected, null))) {
                    vb.tvGoalFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalFilterPriorityTabHigh.setTypeface(vb.tvGoalFilterPriorityTabHigh.typeface, Typeface.BOLD)
                    vb.tvGoalFilterPriorityTabHigh.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalFilterPriorityTabHigh.setTypeface(vb.tvGoalFilterPriorityTabHigh.typeface, Typeface.NORMAL)
                    vb.tvGoalFilterPriorityTabHigh.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalFilterPriorityTabMedium.setOnClickListener {
                if(vb.tvGoalFilterPriorityTabMedium.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.middle_option_default, null))) {
                    vb.tvGoalFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_selected)
                    vb.tvGoalFilterPriorityTabMedium.setTypeface(vb.tvGoalFilterPriorityTabMedium.typeface, Typeface.BOLD)
                    vb.tvGoalFilterPriorityTabMedium.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvGoalFilterPriorityTabMedium.setTypeface(vb.tvGoalFilterPriorityTabMedium.typeface, Typeface.NORMAL)
                    vb.tvGoalFilterPriorityTabMedium.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalFilterPriorityTabLow.setOnClickListener {
                if(vb.tvGoalFilterPriorityTabLow.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvGoalFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalFilterPriorityTabLow.setTypeface(vb.tvGoalFilterPriorityTabLow.typeface, Typeface.BOLD)
                    vb.tvGoalFilterPriorityTabLow.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalFilterPriorityTabLow.setTypeface(vb.tvGoalFilterPriorityTabLow.typeface, Typeface.NORMAL)
                    vb.tvGoalFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                }
            }

            // STATE
            vb.tvGoalFilterStateComplete.setOnClickListener {
                if(vb.tvGoalFilterStateComplete.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_default, null))) {
                    vb.tvGoalFilterStateComplete.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalFilterStateComplete.setTypeface(vb.tvGoalFilterStateComplete.typeface, Typeface.BOLD)
                    vb.tvGoalFilterStateComplete.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalFilterStateComplete.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalFilterStateComplete.setTypeface(vb.tvGoalFilterStateComplete.typeface, Typeface.NORMAL)
                    vb.tvGoalFilterStateComplete.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalFilterStateIncomplete.setOnClickListener {
                if(vb.tvGoalFilterStateIncomplete.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvGoalFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalFilterStateIncomplete.setTypeface(vb.tvGoalFilterStateIncomplete.typeface, Typeface.BOLD)
                    vb.tvGoalFilterStateIncomplete.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalFilterStateIncomplete.setTypeface(vb.tvGoalFilterStateIncomplete.typeface, Typeface.NORMAL)
                    vb.tvGoalFilterStateIncomplete.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalSortTimeExpectedLE.setOnClickListener {
                if(vb.tvGoalSortTimeExpectedLE.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_default, null))) {
                    vb.tvGoalSortTimeExpectedLE.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalSortTimeExpectedLE.setTypeface(vb.tvGoalSortTimeExpectedLE.typeface, Typeface.BOLD)
                    vb.tvGoalSortTimeExpectedLE.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalSortTimeExpectedLE.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortTimeExpectedLE.setTypeface(vb.tvGoalSortTimeExpectedLE.typeface, Typeface.NORMAL)
                    vb.tvGoalSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalSortTimeExpectedLE.setOnClickListener {
                if(vb.tvGoalSortTimeExpectedLE.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvGoalSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalSortTimeExpectedLE.setTypeface(vb.tvGoalSortTimeExpectedLE.typeface, Typeface.BOLD)
                    vb.tvGoalSortTimeExpectedLE.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortTimeExpectedLE.setTypeface(vb.tvGoalSortTimeExpectedLE.typeface, Typeface.NORMAL)
                    vb.tvGoalSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalSortPriorityHL.setOnClickListener {
                if(vb.tvGoalSortPriorityHL.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_default, null))) {
                    vb.tvGoalSortPriorityHL.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalSortPriorityHL.setTypeface(vb.tvGoalSortPriorityHL.typeface, Typeface.BOLD)
                    vb.tvGoalSortPriorityHL.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalSortPriorityHL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortPriorityHL.setTypeface(vb.tvGoalSortPriorityHL.typeface, Typeface.NORMAL)
                    vb.tvGoalSortPriorityHL.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalSortPriorityLH.setOnClickListener {
                if(vb.tvGoalSortPriorityLH.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvGoalSortPriorityLH.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalSortPriorityLH.setTypeface(vb.tvGoalSortPriorityLH.typeface, Typeface.BOLD)
                    vb.tvGoalSortPriorityLH.setTextColor(Color.WHITE)
                } else {
                    vb.tvGoalSortPriorityLH.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortPriorityLH.setTypeface(vb.tvGoalSortPriorityLH.typeface, Typeface.NORMAL)
                    vb.tvGoalSortPriorityLH.setTextColor(Color.DKGRAY)
                }
            }

            vb.clGoalFilterTimeExpectedStartDate.setOnClickListener {
                val dateStartDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvGoalFilterTimeExpectedStartDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInput = year
                    monthInput = monthOfYear
                    dayInput = dayOfMonth
                },  yearInput, monthInput, dayInput)
                dateStartDialog.show()
            }

            vb.clGoalFilterTimeExpectedStartTime.setOnClickListener {
                val timeStartDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvGoalFilterTimeExpectedStartTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInput = hourOfDay
                    minuteInput = minute
                },  hourInput, minuteInput, false)
                timeStartDialog.show()
            }

            vb.clGoalFilterTimeExpectedEndDate.setOnClickListener {
                val dateEndDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvGoalFilterTimeExpectedEndDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInput = year
                    monthInput = monthOfYear
                    dayInput = dayOfMonth
                },  yearInput, monthInput, dayInput)
                dateEndDialog.show()
            }

            vb.clGoalFilterTimeExpectedEndTime.setOnClickListener {
                val timeEndDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvGoalFilterTimeExpectedEndTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInput = hourOfDay
                    minuteInput = minute
                },  hourInput, minuteInput, false)
                timeEndDialog.show()
            }

            vb.btnGoalSortFilterCancel.setOnClickListener {
                d.dismiss()
            }

            // TODO: After closing the dialog, update recycler view contents
            vb.btnGoalSortFilterApply.setOnClickListener {
                // db.sortFilterGoals()
            }

            d.setCancelable(true)
            d.show()
        }

        Log.d("MOBDEVE_MCO", "Main Activity - CREATE")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

//        this.goalsAdapter = GoalAdapter(GoalKeepDatabase(applicationContext).getGoals(), this)
//        this.rv.adapter = this.goalsAdapter

        Log.d("MOBDEVE_MCO", "Main Activity - START")
    }
}