package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.adapter.GoalAdapter
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

    private var yearInputStart : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInputStart : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInputStart : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInputStart : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInputStart : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var yearInputEnd : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInputEnd : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInputEnd : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInputEnd : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInputEnd : Int = Calendar.getInstance().get(Calendar.MINUTE)

    private var mainGoal : Goal? = null
    private lateinit var goalTimeExpected : String
    private var goalId : Int = -1

    @RequiresApi(Build.VERSION_CODES.O)
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

                        val intent = Intent(this, NotificationBroadcaster::class.java)
                        intent.putExtra("NOTIF_MSG", "Reminder to finish ${task.name} at ${task.timeExpected}")
                        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                            PendingIntent.FLAG_IMMUTABLE)

                        val alarmManager : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                        alarmManager.set(AlarmManager.RTC, DateHelper.getMillisecondsTime(task.timeExpected, 1), pendingIntent)
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

    @RequiresApi(Build.VERSION_CODES.O)
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
                if(vb.tvTaskFilterPriorityTabHigh.currentTextColor == Color.DKGRAY) {                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(0)
                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskFilterPriorityTabHigh.setTextColor(Color.WHITE)

                    vb.tvTaskFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvTaskFilterPriorityTabMedium.setTextColor(Color.DKGRAY)

                    vb.tvTaskFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskFilterPriorityTabHigh.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskFilterPriorityTabMedium.setOnClickListener {
                if(vb.tvTaskFilterPriorityTabMedium.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskFilterPriorityTabMedium.setBackgroundResource(0)
                    vb.tvTaskFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_selected)
                    vb.tvTaskFilterPriorityTabMedium.setTextColor(Color.WHITE)

                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskFilterPriorityTabHigh.setTextColor(Color.DKGRAY)

                    vb.tvTaskFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvTaskFilterPriorityTabMedium.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskFilterPriorityTabLow.setOnClickListener {
                if(vb.tvTaskFilterPriorityTabLow.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskFilterPriorityTabLow.setBackgroundResource(0)
                    vb.tvTaskFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskFilterPriorityTabLow.setTextColor(Color.WHITE)

                    vb.tvTaskFilterPriorityTabHigh.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskFilterPriorityTabHigh.setTextColor(Color.DKGRAY)

                    vb.tvTaskFilterPriorityTabMedium.setBackgroundResource(R.drawable.middle_option_default)
                    vb.tvTaskFilterPriorityTabMedium.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskFilterPriorityTabLow.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskFilterPriorityTabLow.setTextColor(Color.DKGRAY)
                }
            }

            // STATE
            vb.tvTaskFilterStateComplete.setOnClickListener {
                if(vb.tvTaskFilterStateComplete.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskFilterStateComplete.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskFilterStateComplete.setTextColor(Color.WHITE)

                    vb.tvTaskFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskFilterStateIncomplete.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskFilterStateComplete.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskFilterStateComplete.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskFilterStateIncomplete.setOnClickListener {
                if(vb.tvTaskFilterStateIncomplete.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskFilterStateIncomplete.setTextColor(Color.WHITE)

                    vb.tvTaskFilterStateComplete.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskFilterStateComplete.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskFilterStateIncomplete.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskFilterStateIncomplete.setTextColor(Color.DKGRAY)
                }
            }

            // TASK NAME
            vb.tvTaskSortNameTabAZ.setOnClickListener {
                if(vb.tvTaskSortNameTabAZ.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskSortNameTabAZ.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskSortNameTabAZ.setTextColor(Color.WHITE)

                    vb.tvTaskSortNameTabZA.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortNameTabZA.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskSortNameTabAZ.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortNameTabAZ.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortNameTabZA.setOnClickListener {
                if(vb.tvTaskSortNameTabZA.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskSortNameTabZA.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskSortNameTabZA.setTextColor(Color.WHITE)

                    vb.tvTaskSortNameTabAZ.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortNameTabAZ.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskSortNameTabZA.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortNameTabZA.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortTimeExpectedEL.setOnClickListener {
                if(vb.tvTaskSortTimeExpectedEL.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskSortTimeExpectedEL.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskSortTimeExpectedEL.setTextColor(Color.WHITE)

                    vb.tvTaskSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskSortTimeExpectedEL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortTimeExpectedEL.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortTimeExpectedLE.setOnClickListener {
                if(vb.tvTaskSortTimeExpectedLE.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskSortTimeExpectedLE.setTextColor(Color.WHITE)

                    vb.tvTaskSortTimeExpectedEL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortTimeExpectedEL.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskSortTimeExpectedLE.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortTimeExpectedLE.setTextColor(Color.DKGRAY)
                }
            }

            // Priority
            vb.tvTaskSortPriorityHL.setOnClickListener {
                if(vb.tvTaskSortPriorityHL.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskSortPriorityHL.setBackgroundResource(R.drawable.left_option_selected)
                    vb.tvTaskSortPriorityHL.setTextColor(Color.WHITE)

                    vb.tvTaskSortPriorityLH.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortPriorityLH.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskSortPriorityHL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortPriorityHL.setTextColor(Color.DKGRAY)
                }
            }

            vb.tvTaskSortPriorityLH.setOnClickListener {
                if(vb.tvTaskSortPriorityLH.currentTextColor == Color.DKGRAY) {
                    vb.tvTaskSortPriorityLH.setBackgroundResource(R.drawable.right_option_selected)
                    vb.tvTaskSortPriorityLH.setTextColor(Color.WHITE)

                    vb.tvTaskSortPriorityHL.setBackgroundResource(R.drawable.left_option_default)
                    vb.tvTaskSortPriorityHL.setTextColor(Color.DKGRAY)
                } else {
                    vb.tvTaskSortPriorityLH.setBackgroundResource(R.drawable.right_option_default)
                    vb.tvTaskSortPriorityLH.setTextColor(Color.DKGRAY)
                }
            }

            vb.clTaskFilterTimeExpectedStartDate.setOnClickListener {
                val dateStartDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvTaskFilterTimeExpectedStartDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInputStart = year
                    monthInputStart = monthOfYear
                    dayInputStart = dayOfMonth
                },  yearInputStart, monthInputStart, dayInputStart)
                dateStartDialog.show()
            }

            vb.clTaskFilterTimeExpectedStartTime.setOnClickListener {
                val timeStartDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvTaskFilterTimeExpectedStartTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInputStart = hourOfDay
                    minuteInputStart = minute
                },  hourInputStart, minuteInputStart, false)
                timeStartDialog.show()
            }

            vb.clTaskFilterTimeExpectedEndDate.setOnClickListener {
                val dateEndDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                    vb.tvTaskFilterTimeExpectedEndDate.text = "${DateHelper.getMonthName(monthOfYear + 1)} $dayOfMonth, $year"
                    yearInputEnd = year
                    monthInputEnd = monthOfYear
                    dayInputEnd = dayOfMonth
                },  yearInputEnd, monthInputEnd, dayInputEnd)
                dateEndDialog.show()
            }

            vb.clTaskFilterTimeExpectedEndTime.setOnClickListener {
                val timeEndDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
                    vb.tvTaskFilterTimeExpectedEndTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getAppendZero(minute)} ${DateHelper.getAMPM(hourOfDay)}"
                    hourInputEnd = hourOfDay
                    minuteInputEnd = minute
                },  hourInputEnd, minuteInputEnd, false)
                timeEndDialog.show()
            }

            vb.btnTaskSortFilterCancel.setOnClickListener {
                d.dismiss()
            }

            vb.btnTaskSortFilterApply.setOnClickListener {
                // FILTER
                val chosenPriority : String
                if(vb.tvTaskFilterPriorityTabHigh.currentTextColor == Color.WHITE)
                    chosenPriority = "High"
                else if(vb.tvTaskFilterPriorityTabMedium.currentTextColor == Color.WHITE)
                    chosenPriority = "Medium"
                else if(vb.tvTaskFilterPriorityTabMedium.currentTextColor == Color.WHITE)
                    chosenPriority = "Low"
                else
                    chosenPriority = "N/A"

                monthInputStart++
                monthInputEnd++
                val startDateTime = DateHelper.getDatabaseTimeFormat(yearInputStart, monthInputStart, dayInputStart, hourInputStart, minuteInputStart)
                val endDateTime = DateHelper.getDatabaseTimeFormat(yearInputEnd, monthInputEnd, dayInputEnd, hourInputEnd, minuteInputEnd)

                val taskState : String
                if(vb.tvTaskFilterStateComplete.currentTextColor == Color.WHITE)
                    taskState = "Complete"
                else if(vb.tvTaskFilterStateIncomplete.currentTextColor == Color.WHITE)
                    taskState = "Incomplete"
                else
                    taskState = "N/A"

                // SORT
                val sortTaskName : String
                if(vb.tvTaskSortNameTabAZ.currentTextColor == Color.WHITE)
                    sortTaskName = "ASC"
                else if(vb.tvTaskSortNameTabZA.currentTextColor == Color.WHITE)
                    sortTaskName = "DESC"
                else
                    sortTaskName = "N/A"

                val sortTimeExpected : String
                if(vb.tvTaskSortTimeExpectedEL.currentTextColor == Color.WHITE)
                    sortTimeExpected = "ASC"
                else if(vb.tvTaskSortTimeExpectedLE.currentTextColor == Color.WHITE)
                    sortTimeExpected = "DESC"
                else
                    sortTimeExpected = "N/A"

                val sortPriority : String
                if(vb.tvTaskSortPriorityLH.currentTextColor == Color.WHITE)
                    sortPriority = "ASC"
                else if(vb.tvTaskSortPriorityHL.currentTextColor == Color.WHITE)
                    sortPriority = "DESC"
                else
                    sortPriority = "N/A"

                val filteredTasks = db.sortFilterTasks(chosenPriority, startDateTime, endDateTime, taskState,
                    sortTaskName, sortTimeExpected, sortPriority)

                sortFilterData(filteredTasks)
                d.dismiss()
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

        vb.tvViewGoalTasks.text = "${db.getRemainingTasksCount(goalId)} Tasks Remaining"

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

    private fun sortFilterData(tasks : ArrayList<Task>) {
        tasksAdapter.clear()
        this.rv = vb.rvTasks
        this.tasksAdapter = TaskAdapter(tasks, this)
        this.rv.adapter = this.tasksAdapter
    }
}