package com.mobdeve.s13.group03.goalkeep.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.mobdeve.s13.group03.goalkeep.DateHelper
import com.mobdeve.s13.group03.goalkeep.model.Goal
import com.mobdeve.s13.group03.goalkeep.model.Task

class GoalKeepDatabase (context : Context) {
    private lateinit var databaseHandler: DatabaseHandler

    init {
        this.databaseHandler = DatabaseHandler(context)
    }

    // GOAL DATABASE OPERATIONS
    fun addGoal(g: Goal) : Long {
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

        var id = db.insert(DatabaseHandler.GOAL_TABLE, null, contentValues)
        Log.d("MOBDEVE_MCO", "Added New Goal ")
        db.close()

        return id
    }

    fun updateGoal(g: Goal) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        // Goal Id should not be updated

        if(g.title != "NO_EDIT")
            contentValues.put(DatabaseHandler.GOAL_TITLE, g.title)

        // Time Created should not be updated

        if(g.timeExpected != "NO_EDIT")
            contentValues.put(DatabaseHandler.GOAL_TIME_EXPECTED, g.timeExpected)

        if(g.timeCompleted != "NO_EDIT")
            contentValues.put(DatabaseHandler.GOAL_TIME_COMPLETED, g.timeCompleted)

        if(g.description != "NO_EDIT")
            contentValues.put(DatabaseHandler.GOAL_DESCRIPTION, g.description)

        if(g.priority != "NO_EDIT")
            contentValues.put(DatabaseHandler.GOAL_PRIORITY, g.priority)

        if(g.state != "NO_EDIT")
            contentValues.put(DatabaseHandler.GOAL_STATE, g.state)

        if(g.state != "NO_EDIT")
            contentValues.put(DatabaseHandler.GOAL_TAG, g.tag)

        db.update(DatabaseHandler.GOAL_TABLE, contentValues,
            "goal_id=?", arrayOf(g.goalId.toLong().toString()))
        db.close()
    }

    fun deleteGoal(goalId : Int) {
        val db = databaseHandler.writableDatabase

        // Delete the tasks under the goal before deleting the goal
        db.delete(DatabaseHandler.TASK_TABLE, "goal_id=?", arrayOf(goalId.toString()))
        db.delete(DatabaseHandler.GOAL_TABLE, "goal_id=?", arrayOf(goalId.toString()))
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

    fun completeGoal (goalId : Int) {
        val db = databaseHandler.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(DatabaseHandler.GOAL_STATE, "Complete")
        contentValues.put(DatabaseHandler.GOAL_TIME_COMPLETED, DateHelper.getCurrentTime())

        db.update(DatabaseHandler.GOAL_TABLE, contentValues, "goal_id=?", arrayOf(goalId.toString()))

        db.close()
    }

    // TASK DATABASE OPERATIONS
    fun addTask(t: Task, goalId: Int) : Long {
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
        Log.d("MOBDEVE_MCO", t.state)
        var id = db.insert(DatabaseHandler.TASK_TABLE, null, contentValues)
        db.close()

        return id
    }

    fun updateTask(t: Task) {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        // Task Id should not be updated

        if(t.name != "NO_EDIT")
            contentValues.put(DatabaseHandler.TASK_TITLE, t.name)

        // Time Created should not be updated

        if(t.timeExpected != "NO_EDIT")
            contentValues.put(DatabaseHandler.TASK_TIME_EXPECTED, t.timeExpected)

        if(t.timeCompleted != "NO_EDIT")
            contentValues.put(DatabaseHandler.TASK_TIME_COMPLETED, t.timeCompleted)

        if(t.description != "NO_EDIT")
            contentValues.put(DatabaseHandler.TASK_DESCRIPTION, t.description)

        if(t.description != "NO_EDIT")
            contentValues.put(DatabaseHandler.TASK_PRIORITY, t.priority)

        if(t.description != "NO_EDIT")
            contentValues.put(DatabaseHandler.TASK_STATE, t.state)

        // Goal Id FK should not be updated

        db.update(DatabaseHandler.TASK_TABLE, contentValues,
                "task_id=?", arrayOf(t.taskId.toLong().toString()))
    }

    fun deleteTask(taskId : Int) {
        val db = databaseHandler.writableDatabase

        db.delete(DatabaseHandler.TASK_TABLE, "task_id=?", arrayOf(taskId.toString()))
        db.close()
    }

    fun getTasks(goalId : Int) : ArrayList<Task> {
        val queriedTasks = ArrayList<Task>()
        val db = databaseHandler.writableDatabase

        val c : Cursor = db.query(DatabaseHandler.TASK_TABLE,
                                  getTaskAttributesArray(),
                                  "goal_id=?",
                                  arrayOf(goalId.toString()),
                                  null,
                                  null,
                                  "CASE ${DatabaseHandler.TASK_PRIORITY} WHEN \'High\' THEN 1 WHEN \'Medium\' THEN 2 ELSE 3 END",
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

    fun updateTaskState(taskId : Int) {
        val db = databaseHandler.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(DatabaseHandler.TASK_STATE, "Complete")

        db.update(DatabaseHandler.TASK_TABLE, contentValues,
            "task_id=?", arrayOf(taskId.toLong().toString()))
    }

    fun getCompletedGoals() : ArrayList<Goal> {
        val queriedCompletedGoals = ArrayList<Goal>()
        val db = databaseHandler.writableDatabase

        val c : Cursor =
            db.query(DatabaseHandler.GOAL_TABLE, getGoalAttributesArray(),
                "state=\'Complete\'",
                null,
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

    @SuppressLint("Recycle")
    fun getSearchQuery(query: String) : ArrayList<Goal> {
        val queriedGoals = ArrayList<Goal>()
        val db = databaseHandler.writableDatabase

        val c : Cursor = db.query(DatabaseHandler.GOAL_TABLE,
                                  getGoalAttributesArray(),
                                  "title LIKE ?",
                                  arrayOf("%${query}%"),
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

    @SuppressLint("Recycle")
    fun sortFilterGoals(chosenPriority : String, startDateTime : String, endDateTime : String, goalState : String,
                        sortGoalName : String, sortTimeExpected : String, sortPriority : String) : ArrayList<Goal> {
        val filteredGoals = ArrayList<Goal>()
        val db = databaseHandler.writableDatabase
        var finalQuery = ""
        var finalOrderByQuery = ""
        val arrayListQuery = ArrayList<String>()

        var prioritySubQuery = ""
        if(chosenPriority != "N/A") {
            prioritySubQuery = "${DatabaseHandler.GOAL_PRIORITY}=\'$chosenPriority\' "
            arrayListQuery.add(chosenPriority)
        }

        var dateRangeSubQuery = ""
        arrayListQuery.add(startDateTime)
        arrayListQuery.add(endDateTime)
        dateRangeSubQuery = "DATE(\'$startDateTime\') <= ${DatabaseHandler.GOAL_TIME_EXPECTED} AND " + "${DatabaseHandler.GOAL_TIME_EXPECTED} <= DATE(\'$endDateTime\')"

        var goalSubQuery = ""
        if(goalState != "N/A") {
            goalSubQuery = "${DatabaseHandler.GOAL_STATE}=\'$goalState\'"
            arrayListQuery.add(goalState)
        }

        // Combine for selection param
        if(prioritySubQuery.isNotEmpty()) {
            finalQuery = "$prioritySubQuery AND $dateRangeSubQuery"
        } else
            finalQuery = dateRangeSubQuery

        if(goalSubQuery.isNotEmpty()) {
            finalQuery = "$finalQuery AND $goalSubQuery"
        }

        var finalArrayQuery = arrayOfNulls<String>(arrayListQuery.size)
        finalArrayQuery = arrayListQuery.toArray(finalArrayQuery)
        
        // SORT
        var sortPrioritySubQuery  = ""
        if(sortPriority != "N/A") {
            when(sortPriority) {
                "ASC" ->
                    sortPrioritySubQuery = "CASE ${DatabaseHandler.GOAL_PRIORITY} WHEN \'High\' THEN 1 WHEN \'Medium\' THEN 2 ELSE 3 END"
                "DESC" ->
                    sortPrioritySubQuery = "CASE ${DatabaseHandler.GOAL_PRIORITY} WHEN \'High\' THEN 3 WHEN \'Medium\' THEN 2 ELSE 1 END"
            }
        }

        var sortTimeExpectedSubQuery = ""
        if(sortTimeExpected != "N/A")
            sortTimeExpectedSubQuery = "${DatabaseHandler.GOAL_TIME_EXPECTED} $sortTimeExpected"

        var sortGoalNameSubQuery = ""
        if(sortGoalName != "N/A")
            sortGoalNameSubQuery = "${DatabaseHandler.GOAL_TITLE} $sortGoalName"

        // Combine for orderBy param
        if(sortPrioritySubQuery.isNotEmpty()) {
            finalOrderByQuery = sortPrioritySubQuery
        }

        if(sortTimeExpectedSubQuery.isNotEmpty()) {
            if(finalOrderByQuery.isEmpty())
                finalOrderByQuery = sortTimeExpectedSubQuery
            else
                finalOrderByQuery = "$finalOrderByQuery, $sortTimeExpectedSubQuery"
        }

        if(sortGoalNameSubQuery.isNotEmpty()) {
            if(finalOrderByQuery.isEmpty())
                finalOrderByQuery = sortGoalNameSubQuery
            else
                finalOrderByQuery = "$finalOrderByQuery, $sortGoalNameSubQuery"
        }

        Log.d("MOBDEVE_MCO", finalQuery)
        Log.d("MOBDEVE_MCO", finalArrayQuery.toString())
        Log.d("MOBDEVE_MCO", finalOrderByQuery)

        val c : Cursor
        if(finalOrderByQuery.isNotEmpty())
            c = db.query(DatabaseHandler.GOAL_TABLE,
                                    getGoalAttributesArray(),
                                    finalQuery,
                                    null,
                                    null,
                                    null,
                                    finalOrderByQuery,
                                    null)
        else
            c = db.query(DatabaseHandler.GOAL_TABLE,
                getGoalAttributesArray(),
                finalQuery,
                null,
                null,
                null,
                null,
                null)

        while(c.moveToNext()) {
            filteredGoals.add(
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

        return filteredGoals
    }

    @SuppressLint("Recycle")
    fun sortFilterTasks(chosenPriority : String, startDateTime : String, endDateTime : String, taskState : String,
                        sortTaskName : String, sortTimeExpected : String, sortPriority : String) : ArrayList<Task> {
        val filteredTasks = ArrayList<Task>()
        val db = databaseHandler.writableDatabase
        var finalQuery = ""
        var finalOrderByQuery = ""
        val arrayListQuery = ArrayList<String>()

        var prioritySubQuery = ""
        if(chosenPriority != "N/A") {
            prioritySubQuery = "${DatabaseHandler.TASK_PRIORITY}=\'$chosenPriority\' "
            arrayListQuery.add(chosenPriority)
        }

        var dateRangeSubQuery = ""
        arrayListQuery.add(startDateTime)
        arrayListQuery.add(endDateTime)
        dateRangeSubQuery = "DATE(\'$startDateTime\') <= ${DatabaseHandler.TASK_TIME_EXPECTED} AND " + "${DatabaseHandler.TASK_TIME_EXPECTED} <= DATE(\'$endDateTime\')"

        var taskSubQuery = ""
        if(taskState != "N/A") {
            taskSubQuery = "${DatabaseHandler.TASK_STATE}=\'$taskState\'"
            arrayListQuery.add(taskState)
        }

        // Combine for selection param
        if(prioritySubQuery.isNotEmpty()) {
            finalQuery = "$prioritySubQuery AND $dateRangeSubQuery"
        } else
            finalQuery = dateRangeSubQuery

        if(taskSubQuery.isNotEmpty()) {
            finalQuery = "$finalQuery AND $taskSubQuery"
        }

        var finalArrayQuery = arrayOfNulls<String>(arrayListQuery.size)
        finalArrayQuery = arrayListQuery.toArray(finalArrayQuery)


        // SORT
        var sortPrioritySubQuery  = ""
        if(sortPriority != "N/A") {
            when(sortPriority) {
                "ASC" ->
                    sortPrioritySubQuery = "CASE ${DatabaseHandler.TASK_PRIORITY} WHEN \'High\' THEN 1 WHEN \'Medium\' THEN 2 ELSE 3 END"
                "DESC" ->
                    sortPrioritySubQuery = "CASE ${DatabaseHandler.TASK_PRIORITY} WHEN \'High\' THEN 3 WHEN \'Medium\' THEN 2 ELSE 1 END"
            }
        }

        var sortTimeExpectedSubQuery = ""
        if(sortTimeExpected != "N/A")
            sortTimeExpectedSubQuery = "${DatabaseHandler.TASK_TIME_EXPECTED} $sortTimeExpected"

        var sortTaskNameSubQuery = ""
        if(sortTaskName != "N/A")
            sortTaskNameSubQuery = "${DatabaseHandler.TASK_TITLE} $sortTaskName"

        // Combine for orderBy param
        if(sortPrioritySubQuery.isNotEmpty()) {
            finalOrderByQuery = sortPrioritySubQuery
        }

        if(sortTimeExpectedSubQuery.isNotEmpty()) {
            if(finalOrderByQuery.isEmpty())
                finalOrderByQuery = sortTimeExpectedSubQuery
            else
                finalOrderByQuery = "$finalOrderByQuery, $sortTimeExpectedSubQuery"
        }

        if(sortTaskNameSubQuery.isNotEmpty()) {
            if(finalOrderByQuery.isEmpty())
                finalOrderByQuery = sortTaskNameSubQuery
            else
                finalOrderByQuery = "$finalOrderByQuery, $sortTaskNameSubQuery"
        }

        val c : Cursor
        if(finalOrderByQuery.isNotEmpty())
            c = db.query(DatabaseHandler.TASK_TABLE,
                getTaskAttributesArray(),
                finalQuery,
                finalArrayQuery,
                null,
                null,
                finalOrderByQuery,
                null)
        else
            c = db.query(DatabaseHandler.TASK_TABLE,
                getTaskAttributesArray(),
                finalQuery,
                finalArrayQuery,
                null,
                null,
                null,
                null)

        Log.d("MOBDEVE_MCO", finalQuery)
        Log.d("MOBDEVE_MCO", finalArrayQuery.toString())
        Log.d("MOBDEVE_MCO", finalOrderByQuery)

        while(c.moveToNext()) {
            filteredTasks.add(
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

        return filteredTasks
    }

    fun getProgressCount(goalId : Int) : Int {
        val db = databaseHandler.writableDatabase
        var completedGoals = 0
        var totalGoals = 0
        val c : Cursor = db.query(DatabaseHandler.TASK_TABLE,
            getTaskAttributesArray(),
            "goal_id=?",
            arrayOf(goalId.toString()),
            null,
            null,
            null,
            null)

        totalGoals = c.count

        while(c.moveToNext()) {
            if(c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_STATE)).equals("Complete"))
                completedGoals += 1
        }

        c.close()
        db.close()

        return if(totalGoals == 0)
            0
        else
            completedGoals * 100 / totalGoals
    }

    fun getRemainingTasksCount(goalId : Int) : Int {
        val db = databaseHandler.writableDatabase
        var completedGoals = 0
        var totalGoals = 0
        val c : Cursor = db.query(DatabaseHandler.TASK_TABLE,
            getTaskAttributesArray(),
            "goal_id=?",
            arrayOf(goalId.toString()),
            null,
            null,
            null,
            null)

        totalGoals = c.count

        while(c.moveToNext()) {
            if(c.getString(c.getColumnIndexOrThrow(DatabaseHandler.TASK_STATE)).equals("Complete"))
                completedGoals += 1
        }

        c.close()
        db.close()

        return totalGoals - completedGoals
    }
}