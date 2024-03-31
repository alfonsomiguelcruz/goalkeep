package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.AlarmManager
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
import android.provider.AlarmClock
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

    private var yearInputStart : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInputStart : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInputStart : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInputStart : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInputStart : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var yearInputEnd : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInputEnd : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInputEnd : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInputEnd : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInputEnd : Int = Calendar.getInstance().get(Calendar.MINUTE)

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

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
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

        vb.ibAlarms.setOnClickListener {
            val i = Intent(AlarmClock.ACTION_SHOW_ALARMS)
            startActivity(i)
//            i.putExtra(AlarmClock.EXTRA_MESSAGE, "Message ni Bakla")
//            i.putExtra(AlarmClock.EXTRA_HOUR, 3)
//            i.putExtra(AlarmClock.EXTRA_MINUTES, 11)
//            i.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
//            if(i.resolveActivity(packageManager) != null)
//                startActivity(i)
        }

        // Connecting Recycler View with Adapter
        val db = GoalKeepDatabase(applicationContext)
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
                if(vb.tvGoalFilterPriorityTabHigh.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalFilterPriorityTabHigh.setBackgroundResource(0)
                    vb.tvGoalFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalFilterPriorityTabHigh.setTextColor(Color.WHITE)

                    vb.tvGoalFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvGoalFilterPriorityTabMedium.setTextColor(Color.DKGRAY)

                    vb.tvGoalFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalFilterPriorityTabHigh.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalFilterPriorityTabMedium.setOnClickListener {
                if(vb.tvGoalFilterPriorityTabMedium.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalFilterPriorityTabMedium.setBackgroundResource(0)
                    vb.tvGoalFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_selected)
                    vb.tvGoalFilterPriorityTabMedium.setTextColor(Color.WHITE)

                    vb.tvGoalFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalFilterPriorityTabHigh.setTextColor(Color.DKGRAY)

                    vb.tvGoalFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvGoalFilterPriorityTabMedium.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalFilterPriorityTabLow.setOnClickListener {
                if(vb.tvGoalFilterPriorityTabLow.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalFilterPriorityTabLow.setBackgroundResource(0)
                    vb.tvGoalFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalFilterPriorityTabLow.setTextColor(Color.WHITE)

                    vb.tvGoalFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalFilterPriorityTabHigh.setTextColor(Color.DKGRAY)

                    vb.tvGoalFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvGoalFilterPriorityTabMedium.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                }
            }

            // STATE
            vb.tvGoalFilterStateComplete.setOnClickListener {
                if(vb.tvGoalFilterStateComplete.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalFilterStateComplete.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalFilterStateComplete.setTextColor(Color.WHITE)

                    vb.tvGoalFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalFilterStateIncomplete.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalFilterStateComplete.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalFilterStateComplete.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalFilterStateIncomplete.setOnClickListener {
                if(vb.tvGoalFilterStateIncomplete.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalFilterStateIncomplete.setTextColor(Color.WHITE)

                    vb.tvGoalFilterStateComplete.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalFilterStateComplete.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalFilterStateIncomplete.setTextColor(Color.DKGRAY)
                }
            }

            // GOAL NAME
            vb.tvGoalSortNameTabAZ.setOnClickListener {
                if(vb.tvGoalSortNameTabAZ.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalSortNameTabAZ.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalSortNameTabAZ.setTextColor(Color.WHITE)

                    vb.tvGoalSortNameTabZA.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortNameTabZA.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalSortNameTabAZ.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortNameTabAZ.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalSortNameTabZA.setOnClickListener {
                if(vb.tvGoalSortNameTabZA.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalSortNameTabZA.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalSortNameTabZA.setTextColor(Color.WHITE)

                    vb.tvGoalSortNameTabAZ.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortNameTabAZ.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalSortNameTabZA.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortNameTabZA.setTextColor(Color.DKGRAY)
                }
            }

            // TIME EXPECTED
            vb.tvGoalSortTimeExpectedEL.setOnClickListener {
                if(vb.tvGoalSortTimeExpectedEL.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalSortTimeExpectedEL.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalSortTimeExpectedEL.setTextColor(Color.WHITE)

                    vb.tvGoalSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalSortTimeExpectedEL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortTimeExpectedEL.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalSortTimeExpectedLE.setOnClickListener {
                if(vb.tvGoalSortTimeExpectedLE.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalSortTimeExpectedLE.setTextColor(Color.WHITE)

                    vb.tvGoalSortTimeExpectedEL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortTimeExpectedEL.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                }
            }

            // Priority
            vb.tvGoalSortPriorityHL.setOnClickListener {
                if(vb.tvGoalSortPriorityHL.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalSortPriorityHL.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvGoalSortPriorityHL.setTextColor(Color.WHITE)

                    vb.tvGoalSortPriorityLH.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortPriorityLH.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalSortPriorityHL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortPriorityHL.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvGoalSortPriorityLH.setOnClickListener {
                if(vb.tvGoalSortPriorityLH.currentTextColor == Color.DKGRAY) {
                    vb.tvGoalSortPriorityLH.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvGoalSortPriorityLH.setTextColor(Color.WHITE)

                    vb.tvGoalSortPriorityHL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvGoalSortPriorityHL.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvGoalSortPriorityLH.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvGoalSortPriorityLH.setTextColor(Color.DKGRAY)
                }
            }

            vb.clGoalFilterTimeExpectedStartDate.setOnClickListener {
                val dateStartDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvGoalFilterTimeExpectedStartDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInputStart = year
                    monthInputStart = monthOfYear
                    dayInputStart = dayOfMonth
                },  yearInputStart, monthInputStart, dayInputStart)
                dateStartDialog.show()
            }

            vb.clGoalFilterTimeExpectedStartTime.setOnClickListener {
                val timeStartDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvGoalFilterTimeExpectedStartTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInputStart = hourOfDay
                    minuteInputStart = minute
                },  hourInputStart, minuteInputStart, false)
                timeStartDialog.show()
            }

            vb.clGoalFilterTimeExpectedEndDate.setOnClickListener {
                val dateEndDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvGoalFilterTimeExpectedEndDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInputEnd = year
                    monthInputEnd = monthOfYear
                    dayInputEnd = dayOfMonth
                },  yearInputEnd, monthInputEnd, dayInputEnd)
                dateEndDialog.show()
            }

            vb.clGoalFilterTimeExpectedEndTime.setOnClickListener {
                val timeEndDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvGoalFilterTimeExpectedEndTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInputEnd = hourOfDay
                    minuteInputEnd = minute
                },  hourInputEnd, minuteInputEnd, false)
                timeEndDialog.show()
            }

            vb.btnGoalSortFilterCancel.setOnClickListener {
                d.dismiss()
            }


            vb.btnGoalSortFilterApply.setOnClickListener {
                // FILTER
                val chosenPriority : String
                if(vb.tvGoalFilterPriorityTabHigh.currentTextColor == Color.WHITE)
                    chosenPriority = "High"
                else if(vb.tvGoalFilterPriorityTabMedium.currentTextColor == Color.WHITE)
                    chosenPriority = "Medium"
                else if(vb.tvGoalFilterPriorityTabMedium.currentTextColor == Color.WHITE)
                    chosenPriority = "Low"
                else
                    chosenPriority = "N/A"

                monthInputStart++
                monthInputEnd++
                val startDateTime = DateHelper.getDatabaseTimeFormat(yearInputStart, monthInputStart, dayInputStart, hourInputStart, minuteInputStart)
                val endDateTime = DateHelper.getDatabaseTimeFormat(yearInputEnd, monthInputEnd, dayInputEnd, hourInputEnd, minuteInputEnd)

                val goalState : String
                if(vb.tvGoalFilterStateComplete.currentTextColor == Color.WHITE)
                    goalState = "Complete"
                else if(vb.tvGoalFilterStateIncomplete.currentTextColor == Color.WHITE)
                    goalState = "Incomplete"
                else
                    goalState = "N/A"

                // SORT
                val sortGoalName : String
                if(vb.tvGoalSortNameTabAZ.currentTextColor == Color.WHITE)
                    sortGoalName = "ASC"
                else if(vb.tvGoalSortNameTabZA.currentTextColor == Color.WHITE)
                    sortGoalName = "DESC"
                else
                    sortGoalName = "N/A"

                val sortTimeExpected : String
                if(vb.tvGoalSortTimeExpectedEL.currentTextColor == Color.WHITE)
                    sortTimeExpected = "ASC"
                else if(vb.tvGoalSortTimeExpectedLE.currentTextColor == Color.WHITE)
                    sortTimeExpected = "DESC"
                else
                    sortTimeExpected = "N/A"

                val sortPriority : String
                if(vb.tvGoalSortPriorityLH.currentTextColor == Color.WHITE)
                    sortPriority = "ASC"
                else if(vb.tvGoalSortPriorityHL.currentTextColor == Color.WHITE)
                    sortPriority = "DESC"
                else
                    sortPriority = "N/A"

                val filteredGoals = db.sortFilterGoals(chosenPriority, startDateTime, endDateTime, goalState,
                                                       sortGoalName, sortTimeExpected, sortPriority)
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