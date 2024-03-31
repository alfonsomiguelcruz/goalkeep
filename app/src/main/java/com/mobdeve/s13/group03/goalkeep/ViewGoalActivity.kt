package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.adapter.TaskAdapter
import com.mobdeve.s13.group03.goalkeep.database.DatabaseHandler
import com.mobdeve.s13.group03.goalkeep.database.GoalKeepDatabase
import com.mobdeve.s13.group03.goalkeep.databinding.TaskSortFilterDialogLayoutBinding
import com.mobdeve.s13.group03.goalkeep.databinding.ViewGoalLayoutBinding
import com.mobdeve.s13.group03.goalkeep.model.Goal
import com.mobdeve.s13.group03.goalkeep.model.Task
import java.util.Calendar
import kotlin.math.roundToInt

class ViewGoalActivity : AppCompatActivity() {
    private lateinit var vb : ViewGoalLayoutBinding
    private lateinit var rv : RecyclerView
    private lateinit var tasksAdapter: TaskAdapter
    private lateinit var tasksTouchHelper: ItemTouchHelper

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var mainGoal : Goal? = null
    private lateinit var goalTimeExpected : String
    private var goalId : Int = -1

    private val addTaskResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.data != null) {
                if(result.resultCode == ResultCodes.ADD_TASK.ordinal) {
                    val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data!!.getParcelableExtra(IntentKeys.TASK_OBJECT_KEY.name, Task::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        result.data!!.getParcelableExtra(IntentKeys.TASK_OBJECT_KEY.name)
                    }

                    val goalId = result.data!!.getIntExtra(IntentKeys.TASK_GOAL_ID_KEY.name, -1)

                    if(task != null && goalId != -1) {
                        if (this.tasksAdapter.itemCount == 0) {
                            vb.rvTasks.visibility = View.GONE
                            vb.tvViewGoalEmpty.visibility = View.VISIBLE
                        } else {
                            vb.rvTasks.visibility = View.VISIBLE
                            vb.tvViewGoalEmpty.visibility = View.GONE
                        }

                        tasksAdapter.addTaskItem(task, goalId)
                    }
                }
            }
        }

    @SuppressLint("SetTextI18n")
    private val edit_deleteGoalResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.data != null) {
            if(result.resultCode == ResultCodes.EDIT_GOAL.ordinal) {
                val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data!!.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name, Goal::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    result.data!!.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name)
                }

                // Update View Goal's contents
                if(goal != null) {
                    vb.tvViewGoalTitle.text = goal.title
                    vb.tvViewGoalTimeExpected.text = goal.timeExpected
                    vb.tvViewGoalPriority.text = "${goal.priority} Priority"
                    vb.tvViewGoalTag.text = goal.tag
                    vb.tvViewGoalDescription.text = goal.description
                    vb.llViewGoal.setBackgroundResource(DesignClass.getRegularColor(goal.priority))
                    vb.pbViewGoal.setBackgroundResource(DesignClass.getSubColor(goal.priority))
                    vb.tvViewGoalTag.background.setTint(DesignClass.getSubColor("N/A"))

                    mainGoal = goal
                }
            } else if(result.resultCode == ResultCodes.DELETE_GOAL.ordinal) {
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ViewGoalLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val intent = intent
        mainGoal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name, Goal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name)
        }

        if (mainGoal != null) {
            vb.tvViewGoalTitle.text = mainGoal!!.title
            vb.tvViewGoalTimeExpected.text = mainGoal!!.timeExpected
            vb.tvViewGoalPriority.text = "${mainGoal!!.priority} Priority"
            vb.tvViewGoalTag.text = mainGoal!!.tag
            vb.tvViewGoalDescription.text = mainGoal!!.description
            vb.llViewGoal.setBackgroundResource(DesignClass.getRegularColor(mainGoal!!.priority))
            vb.tvViewGoalTag.background.setTint(DesignClass.getSubColor("N/A"))
            vb.pbViewGoal.progress = GoalKeepDatabase(applicationContext).getProgressCount(mainGoal!!.goalId)
            vb.pbViewGoal.secondaryProgress = 100
            goalId = mainGoal!!.goalId
            goalTimeExpected = mainGoal!!.timeExpected
        }


        vb.fabEditGoal.setOnClickListener {
            val editGoalIntent = Intent(this, EditGoalActivity::class.java)
            editGoalIntent.putExtra(IntentKeys.GOAL_OBJECT_KEY.name, mainGoal!!)
            edit_deleteGoalResultLauncher.launch(editGoalIntent)
        }

        vb.fabAddTask.setOnClickListener {
            val addTaskIntent = Intent(this, AddTaskActivity::class.java)
            addTaskIntent.putExtra(IntentKeys.TASK_GOAL_ID_KEY.name, goalId)
            addTaskIntent.putExtra(IntentKeys.GOAL_TIME_EXPECTED_KEY.name, goalTimeExpected)
            addTaskResultLauncher.launch(addTaskIntent)
        }

        val db = GoalKeepDatabase(applicationContext)
        this.rv = vb.rvTasks
        this.tasksAdapter = TaskAdapter(db.getTasks(goalId), this)
        this.rv.adapter = tasksAdapter

        val verticalLM = LinearLayoutManager(this)
        verticalLM.orientation = LinearLayoutManager.VERTICAL
        vb.rvTasks.layoutManager = verticalLM

        val taskSwipeCallback = TaskSwipeCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, this)
        taskSwipeCallback.taskAdapter = this.tasksAdapter
        tasksTouchHelper = ItemTouchHelper(taskSwipeCallback)
        tasksTouchHelper.attachToRecyclerView(this.rv)

        vb.etTaskSearchbar.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                vb.ibTaskFilter.visibility = View.GONE
                vb.ibTaskAlarm.visibility = View.GONE
            } else {
                vb.ibTaskFilter.visibility = View.VISIBLE
                vb.ibTaskAlarm.visibility = View.VISIBLE
            }
        }

        vb.ibTaskAlarm.setOnClickListener {
            val timeEndDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                hourInput = hourOfDay
                minuteInput = minute

                val i = Intent(AlarmClock.ACTION_SET_ALARM)
                i.putExtra(AlarmClock.EXTRA_MESSAGE, vb.tvViewGoalTitle.text)
                i.putExtra(AlarmClock.EXTRA_HOUR, hourInput)
                i.putExtra(AlarmClock.EXTRA_MINUTES, minuteInput)
                i.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                if(i.resolveActivity(packageManager) != null)
                    startActivity(i)
            },  hourInput, minuteInput, false)
            timeEndDialog.show()
        }

        vb.ibTaskFilter.setOnClickListener {
            val d = Dialog(this)
            val vb = TaskSortFilterDialogLayoutBinding.inflate(layoutInflater)

            d.setContentView(vb.root)

            vb.tvTaskFilterTimeExpectedStartDate.text = ""
            vb.tvTaskFilterTimeExpectedEndDate.text = ""
            vb.tvTaskFilterTimeExpectedStartTime.text = ""
            vb.tvTaskFilterTimeExpectedEndTime.text = ""

            // PRIORITY
            vb.tvTaskFilterPriorityTabHigh.setOnClickListener {
                if(vb.tvTaskFilterPriorityTabHigh.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_selected, null))) {
                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskFilterPriorityTabHigh.setTypeface(vb.tvTaskFilterPriorityTabHigh.typeface, Typeface.BOLD)
                    vb.tvTaskFilterPriorityTabHigh.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskFilterPriorityTabHigh.setTypeface(vb.tvTaskFilterPriorityTabHigh.typeface, Typeface.NORMAL)
                    vb.tvTaskFilterPriorityTabHigh.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskFilterPriorityTabMedium.setOnClickListener {
                if(vb.tvTaskFilterPriorityTabMedium.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.middle_option_default, null))) {
                    vb.tvTaskFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_selected)
                    vb.tvTaskFilterPriorityTabMedium.setTypeface(vb.tvTaskFilterPriorityTabMedium.typeface, Typeface.BOLD)
                    vb.tvTaskFilterPriorityTabMedium.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvTaskFilterPriorityTabMedium.setTypeface(vb.tvTaskFilterPriorityTabMedium.typeface, Typeface.NORMAL)
                    vb.tvTaskFilterPriorityTabMedium.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskFilterPriorityTabLow.setOnClickListener {
                if(vb.tvTaskFilterPriorityTabLow.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvTaskFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskFilterPriorityTabLow.setTypeface(vb.tvTaskFilterPriorityTabLow.typeface, Typeface.BOLD)
                    vb.tvTaskFilterPriorityTabLow.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskFilterPriorityTabLow.setTypeface(vb.tvTaskFilterPriorityTabLow.typeface, Typeface.NORMAL)
                    vb.tvTaskFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                }
            }

            // STATE
            vb.tvTaskFilterStateComplete.setOnClickListener {
                if(vb.tvTaskFilterStateComplete.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_default, null))) {
                    vb.tvTaskFilterStateComplete.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskFilterStateComplete.setTypeface(vb.tvTaskFilterStateComplete.typeface, Typeface.BOLD)
                    vb.tvTaskFilterStateComplete.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskFilterStateComplete.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskFilterStateComplete.setTypeface(vb.tvTaskFilterStateComplete.typeface, Typeface.NORMAL)
                    vb.tvTaskFilterStateComplete.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskFilterStateIncomplete.setOnClickListener {
                if(vb.tvTaskFilterStateIncomplete.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvTaskFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskFilterStateIncomplete.setTypeface(vb.tvTaskFilterStateIncomplete.typeface, Typeface.BOLD)
                    vb.tvTaskFilterStateIncomplete.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskFilterStateIncomplete.setTypeface(vb.tvTaskFilterStateIncomplete.typeface, Typeface.NORMAL)
                    vb.tvTaskFilterStateIncomplete.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortTimeExpectedLE.setOnClickListener {
                if(vb.tvTaskSortTimeExpectedLE.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_default, null))) {
                    vb.tvTaskSortTimeExpectedLE.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskSortTimeExpectedLE.setTypeface(vb.tvTaskSortTimeExpectedLE.typeface, Typeface.BOLD)
                    vb.tvTaskSortTimeExpectedLE.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskSortTimeExpectedLE.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortTimeExpectedLE.setTypeface(vb.tvTaskSortTimeExpectedLE.typeface, Typeface.NORMAL)
                    vb.tvTaskSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortTimeExpectedLE.setOnClickListener {
                if(vb.tvTaskSortTimeExpectedLE.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvTaskSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskSortTimeExpectedLE.setTypeface(vb.tvTaskSortTimeExpectedLE.typeface, Typeface.BOLD)
                    vb.tvTaskSortTimeExpectedLE.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortTimeExpectedLE.setTypeface(vb.tvTaskSortTimeExpectedLE.typeface, Typeface.NORMAL)
                    vb.tvTaskSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortPriorityHL.setOnClickListener {
                if(vb.tvTaskSortPriorityHL.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.left_option_default, null))) {
                    vb.tvTaskSortPriorityHL.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskSortPriorityHL.setTypeface(vb.tvTaskSortPriorityHL.typeface, Typeface.BOLD)
                    vb.tvTaskSortPriorityHL.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskSortPriorityHL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortPriorityHL.setTypeface(vb.tvTaskSortPriorityHL.typeface, Typeface.NORMAL)
                    vb.tvTaskSortPriorityHL.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortPriorityLH.setOnClickListener {
                if(vb.tvTaskSortPriorityLH.background.equals(ResourcesCompat.getDrawable(resources, R.drawable.right_option_default, null))) {
                    vb.tvTaskSortPriorityLH.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskSortPriorityLH.setTypeface(vb.tvTaskSortPriorityLH.typeface, Typeface.BOLD)
                    vb.tvTaskSortPriorityLH.setTextColor(Color.WHITE)
                } else {
                    vb.tvTaskSortPriorityLH.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortPriorityLH.setTypeface(vb.tvTaskSortPriorityLH.typeface, Typeface.NORMAL)
                    vb.tvTaskSortPriorityLH.setTextColor(Color.DKGRAY)
                }
            }

            vb.clTaskFilterTimeExpectedStartDate.setOnClickListener {
                val dateStartDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvTaskFilterTimeExpectedStartDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInput = year
                    monthInput = monthOfYear
                    dayInput = dayOfMonth
                },  yearInput, monthInput, dayInput)
                dateStartDialog.show()
            }

            vb.clTaskFilterTimeExpectedStartTime.setOnClickListener {
                val timeStartDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvTaskFilterTimeExpectedStartTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInput = hourOfDay
                    minuteInput = minute
                },  hourInput, minuteInput, false)
                timeStartDialog.show()
            }

            vb.clTaskFilterTimeExpectedEndDate.setOnClickListener {
                val dateEndDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvTaskFilterTimeExpectedEndDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInput = year
                    monthInput = monthOfYear
                    dayInput = dayOfMonth
                },  yearInput, monthInput, dayInput)
                dateEndDialog.show()
            }

            vb.clTaskFilterTimeExpectedEndTime.setOnClickListener {
                val timeEndDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvTaskFilterTimeExpectedEndTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInput = hourOfDay
                    minuteInput = minute
                },  hourInput, minuteInput, false)
                timeEndDialog.show()
            }

            vb.btnTaskSortFilterCancel.setOnClickListener {
                d.dismiss()
            }

            // TODO: After closing the dialog, update recycler view contents
            vb.btnTaskSortFilterApply.setOnClickListener {
                // db.sortFilterTasks()
            }

            d.setCancelable(true)
            d.show()
        }

        vb.btnCompleteGoal.setOnClickListener {
            Log.d("MOBDEVE_MCO", "GOAL PROGRESS: ${vb.pbViewGoal.progress}")
            if(vb.pbViewGoal.progress == 100) {
                vb.btnCompleteGoal.isEnabled = true
                db.updateTaskState(goalId)
                finish()
            }
        }

        vb.tvViewGoalTasks.text = "${db.getRemainingTasksCount(goalId)}"

        if (this.tasksAdapter.itemCount == 0) {
            vb.rvTasks.visibility = View.GONE
            vb.tvViewGoalEmpty.visibility = View.VISIBLE
        } else {
            vb.rvTasks.visibility = View.VISIBLE
            vb.tvViewGoalEmpty.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        vb.pbViewGoal.progress = GoalKeepDatabase(applicationContext).getProgressCount(goalId)
    }
}