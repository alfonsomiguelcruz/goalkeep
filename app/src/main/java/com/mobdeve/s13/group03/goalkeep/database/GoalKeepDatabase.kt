package com.mobdeve.s13.group03.goalkeep.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.mobdeve.s13.group03.goalkeep.model.Goal
import com.mobdeve.s13.group03.goalkeep.model.Task

class GoalKeepDatabase (context : Context) {
    private lateinit var databaseHandler: DatabaseHandler

    init {
        this.databaseHandler = DatabaseHandler(context)
    }

    // GOAL DATABASE OPERATIONS
    fun addGoal(g: Goal) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        // Goal Id should not be added
        contentValues.put(DatabaseHandler.GOAL_TITLE, g.title)
        contentValues.put(DatabaseHandler.GOAL_TIME_CREATED, g.timeCreated)
        contentValues.put(DatabaseHandler.GOAL_TIME_EXPECTED, g.timeExpected)
        contentValues.put(DatabaseHandler.GOAL_TIME_COMPLETED, g.timeCompleted)
        contentValues.put(DatabaseHandler.GOAL_DESCRIPTION, g.description)
        contentValues.put(DatabaseHandler.GOAL_PRIORITY, g.priority)
        contentValues.put(DatabaseHandler.GOAL_STATE, g.state)
        contentValues.put(DatabaseHandler.GOAL_TAG, g.tag)

        db.insert(DatabaseHandler.GOAL_TABLE, null, contentValues)
        Log.d("MOBDEVE_MCO", "Added New Goal")
        db.close()
    }

    fun updateGoal(g: Goal) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        // Goal Id should not be updated
        contentValues.put(DatabaseHandler.GOAL_TITLE, g.title)
        // Time Created should not be updated
        contentValues.put(DatabaseHandler.GOAL_TIME_EXPECTED, g.timeExpected)
        contentValues.put(DatabaseHandler.GOAL_TIME_COMPLETED, g.timeCompleted)
        contentValues.put(DatabaseHandler.GOAL_DESCRIPTION, g.description)
        contentValues.put(DatabaseHandler.GOAL_PRIORITY, g.priority)
        contentValues.put(DatabaseHandler.GOAL_STATE, g.state)
        contentValues.put(DatabaseHandler.GOAL_TAG, g.tag)

        db.update(DatabaseHandler.GOAL_TABLE, contentValues,
            "goal_id=?", arrayOf(g.goalId.toString()))
        db.close()
    }

    fun deleteGoal(g: Goal) {
        val db = databaseHandler.writableDatabase

        // Delete the tasks under the goal before deleting the goal
        db.delete(DatabaseHandler.TASK_TABLE, "goal_id=?", arrayOf(g.goalId.toString()))
        db.delete(DatabaseHandler.GOAL_TABLE, "goal_id=?", arrayOf(g.goalId.toString()))
        db.close()
    }

    fun getGoals() : ArrayList<Goal> {
        val queriedGoals = ArrayList<Goal>()
        val db = databaseHandler.writableDatabase

        val c : Cursor =
            db.query(DatabaseHandler.GOAL_TABLE, getGoalAttributesArray(),
               null,
               null,
               null,
               null,
                "CASE ${DatabaseHandler.GOAL_PRIORITY} WHEN \'High\' THEN 1 WHEN \'Medium\' THEN 2 ELSE 3 END",
               null)

        while(c.moveToNext()) {
            queriedGoals.add(
                Goal(c.getInt(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_ID)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TITLE)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TIME_CREATED)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TIME_EXPECTED)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TIME_COMPLETED)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_DESCRIPTION)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_PRIORITY)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_STATE)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TAG)))
            )
        }

        c.close()
        db.close()
        return queriedGoals
    }

    fun getGoalId(g: Goal) : Int {
        val db = databaseHandler.writableDatabase
        var goalId : Int = -1

        val c : Cursor = db.query(DatabaseHandler.GOAL_TABLE,
                                  arrayOf(DatabaseHandler.GOAL_ID),
                                  "title=?",
                                  arrayOf(g.title),
                                  null,
                                  null,
                                  null,
                                  null)


        while(c.moveToNext())
            goalId = c.getInt(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_ID))

        c.close()
        db.close()

        return goalId
    }

    private fun getGoalAttributesArray() : Array<String> {
        return arrayOf(
            DatabaseHandler.GOAL_ID,
            DatabaseHandler.GOAL_TITLE,
            DatabaseHandler.GOAL_TIME_CREATED,
            DatabaseHandler.GOAL_TIME_EXPECTED,
            DatabaseHandler.GOAL_TIME_COMPLETED,
            DatabaseHandler.GOAL_DESCRIPTION,
            DatabaseHandler.GOAL_PRIORITY,
            DatabaseHandler.GOAL_STATE,
            DatabaseHandler.GOAL_TAG
        )
    }

    // TASK DATABASE OPERATIONS
    fun addTask(t: Task, goalId: Int) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        // Task Id should not be added
        contentValues.put(DatabaseHandler.TASK_TITLE, t.name)
        contentValues.put(DatabaseHandler.TASK_TIME_CREATED, t.timeCreated)
        contentValues.put(DatabaseHandler.TASK_TIME_EXPECTED, t.timeExpected)
        contentValues.put(DatabaseHandler.TASK_TIME_COMPLETED, t.timeCompleted)
        contentValues.put(DatabaseHandler.TASK_DESCRIPTION, t.description)
        contentValues.put(DatabaseHandler.TASK_PRIORITY, t.priority)
        contentValues.put(DatabaseHandler.TASK_STATE, t.state)
        contentValues.put(DatabaseHandler.TASK_GOAL_ID, goalId)

        db.insert(DatabaseHandler.TASK_TABLE, null, contentValues)
        db.close()
    }

    fun updateTask(t: Task) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        // Task Id should not be updated
        contentValues.put(DatabaseHandler.TASK_TITLE, t.name)
        // Time Created should not be updated
        contentValues.put(DatabaseHandler.TASK_TIME_EXPECTED, t.timeExpected)
        contentValues.put(DatabaseHandler.TASK_TIME_COMPLETED, t.timeCompleted)
        contentValues.put(DatabaseHandler.TASK_DESCRIPTION, t.description)
        contentValues.put(DatabaseHandler.TASK_PRIORITY, t.priority)
        contentValues.put(DatabaseHandler.TASK_STATE, t.state)
        // Goal Id FK should not be updated

        db.update(DatabaseHandler.TASK_TABLE, contentValues,
                "task_id=?", arrayOf(t.taskId.toString()))
    }

    fun deleteTask(t: Task) {
        val db = databaseHandler.writableDatabase

        db.delete(DatabaseHandler.TASK_TABLE, "task_id=?", arrayOf(t.taskId.toString()))
        db.close()
    }

    fun getTasks(goalId : Int) : ArrayList<Task> {
        // Use goal id to get the tasks (INNER JOIN on goal id = task's goal id)
        // for View Goal
        val queriedTasks = ArrayList<Task>()
        val db = databaseHandler.writableDatabase

        val c : Cursor = db.query(DatabaseHandler.TASK_TABLE,
                                  getTaskAttributesArray(),
                                  "goal_id=?",
                                  arrayOf(goalId.toString()),
                                  null,
                                  null,
                                  null,
                                  null)

        while(c.moveToNext()) {
            queriedTasks.add(
                Task(c.getInt(c.getColumnIndexOrThrow(DatabaseHandler.TASK_ID)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_TITLE)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_TIME_CREATED)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_TIME_EXPECTED)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_TIME_COMPLETED)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_DESCRIPTION)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_PRIORITY)),
                     c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_STATE)),
                     c.getInt(c.getColumnIndexOrThrow(DatabaseHandler.TASK_GOAL_ID)))
            )
        }

        c.close()
        db.close()

        return queriedTasks
    }

    fun getTaskId(g: Goal) : Int {
        val db = databaseHandler.writableDatabase
        var taskId : Int = -1

        val c : Cursor = db.query(DatabaseHandler.TASK_TABLE,
            arrayOf(DatabaseHandler.TASK_ID),
            "goal_id=?",
            arrayOf(g.goalId.toString()),
            null,
            null,
            null,
            null)


        while(c.moveToNext())
            taskId = c.getInt(c.getColumnIndexOrThrow(DatabaseHandler.TASK_ID))

        c.close()
        db.close()

        return taskId
    }

    private fun getTaskAttributesArray() : Array<String> {
        return arrayOf(
            DatabaseHandler.TASK_ID,
            DatabaseHandler.TASK_TITLE,
            DatabaseHandler.TASK_TIME_CREATED,
            DatabaseHandler.TASK_TIME_EXPECTED,
            DatabaseHandler.TASK_TIME_COMPLETED,
            DatabaseHandler.TASK_DESCRIPTION,
            DatabaseHandler.TASK_PRIORITY,
            DatabaseHandler.TASK_STATE,
            DatabaseHandler.TASK_GOAL_ID
        )
    }

    fun getCompletedGoals() : ArrayList<Goal> {
        val queriedCompletedGoals = ArrayList<Goal>()
        val db = databaseHandler.writableDatabase

        val c : Cursor =
            db.query(DatabaseHandler.GOAL_TABLE, getGoalAttributesArray(),
                "state=?",
                arrayOf("Complete"),
                null,
                null,
                null,
                null)

        while(c.moveToNext()) {
            queriedCompletedGoals.add(
                Goal(c.getInt(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_ID)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TITLE)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TIME_CREATED)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TIME_EXPECTED)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TIME_COMPLETED)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_DESCRIPTION)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_PRIORITY)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_STATE)),
                    c.getString(c.getColumnIndexOrThrow(DatabaseHandler.GOAL_TAG)))
            )
        }

        c.close()
        db.close()
        return queriedCompletedGoals
    }
    // TODO: FILTER and SORT OPERATIONS
}