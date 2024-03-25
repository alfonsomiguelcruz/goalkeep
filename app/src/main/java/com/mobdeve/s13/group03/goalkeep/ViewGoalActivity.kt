package com.mobdeve.s13.group03.goalkeep

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.databinding.TaskSortFilterDialogLayoutBinding
import com.mobdeve.s13.group03.goalkeep.databinding.ViewGoalLayoutBinding
import java.util.Calendar
import kotlin.math.roundToInt

class ViewGoalActivity : AppCompatActivity() {
    private var tasks : ArrayList<Task> = TaskDataGenerator.generateTaskData()
    private lateinit var vb : ViewGoalLayoutBinding
    private lateinit var rv : RecyclerView
    private lateinit var tasksAdapter: TaskAdapter
    private lateinit var tasksTouchHelper: ItemTouchHelper

    private var yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
    private var monthInput : Int = Calendar.getInstance().get(Calendar.MONTH)
    private var dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var hourInput : Int = Calendar.getInstance().get(Calendar.HOUR)
    private var minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ViewGoalLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val intent = intent
        val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name, Goal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(IntentKeys.GOAL_OBJECT_KEY.name)
        }

        if (goal != null) {
            vb.tvViewGoalTitle.text = goal.title
            vb.tvViewGoalTimeExpected.text = goal.timeExpected
            vb.tvViewGoalPriority.text = goal.priority
            vb.tvViewGoalTag.text = goal.tag
            vb.tvViewGoalDescription.text = goal.description
            vb.llViewGoal.setBackgroundResource(DesignClass.getRegularColor(goal.priority))
            vb.pbViewGoal.setBackgroundResource(DesignClass.getSubColor(goal.priority))
            vb.tvViewGoalTag.background.setTint(DesignClass.getSubColor("N/A"))
        }

        // TODO: Find way to edit display of button
        vb.btnCompleteGoal.isEnabled = false

        vb.fabEditGoal.setOnClickListener {
            val editGoalIntent = Intent(this, EditGoalActivity::class.java)
            editGoalIntent.putExtra(IntentKeys.GOAL_OBJECT_KEY.name, goal)
            startActivity(editGoalIntent)
        }

        vb.fabAddTask.setOnClickListener {
            val addTaskIntent = Intent(this, AddTaskTitleActivity::class.java)
            startActivity(addTaskIntent)
        }

        this.rv = vb.rvTasks
        this.tasksAdapter = TaskAdapter(tasks)
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
            } else {
                vb.ibTaskFilter.visibility = View.VISIBLE
            }
        }

        vb.ibTaskFilter.setOnClickListener {
            val d = Dialog(this)
//            val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val vb = TaskSortFilterDialogLayoutBinding.inflate(layoutInflater)

            d.setContentView(vb.root)

            vb.tvTaskFilterTimeExpectedStartDate.text = ""
            vb.tvTaskFilterTimeExpectedEndDate.text = ""
            vb.tvTaskFilterTimeExpectedStartTime.text = ""
            vb.tvTaskFilterTimeExpectedEndTime.text = ""

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
                    vb.tvTaskFilterTimeExpectedStartTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getMinuteFormat(minute)} ${DateHelper.getAMPM(hourOfDay)}"
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
                    vb.tvTaskFilterTimeExpectedEndTime.text = "${DateHelper.getNonMilitaryHour(hourOfDay)}: ${DateHelper.getMinuteFormat(minute)} ${DateHelper.getAMPM(hourOfDay)}"
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
                d.dismiss()
            }

            d.setCancelable(true)
            d.show()
        }
    }

    private fun computeProgress(g: Goal): Int {
        var totalMatched: Int = 0
        var totalComplete: Int = 0

//        for(i in 0..<tasksList.size)
//            if(tasksList[i].goalId == g.goalId) {
//                totalMatched++
//                if (tasksList[i].state == "Complete")
//                    totalComplete++
//            }
        totalMatched = 10
        totalComplete = 5

        val progress: Int = try {
            ((totalComplete * 1.0 / totalMatched) * 100).roundToInt()
        } catch (e: ArithmeticException) {
            0
        }

        return progress
    }
}