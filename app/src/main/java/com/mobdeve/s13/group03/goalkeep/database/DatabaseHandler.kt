package com.mobdeve.s13.group03.goalkeep.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context : Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "GoalKeepDatabase"

        // GOAL_TABLE
        const val GOAL_TABLE = "goals"
        // GOAL_TABLE ATTRIBUTES
        const val GOAL_ID = "goal_id"
        const val GOAL_TITLE = "title"
        const val GOAL_TIME_CREATED = "time_created"
        const val GOAL_TIME_EXPECTED = "time_expected"
        const val GOAL_TIME_COMPLETED = "time_completed"
        const val GOAL_DESCRIPTION = "description"
        const val GOAL_PRIORITY = "priority"
        const val GOAL_STATE = "state"
        const val GOAL_TAG = "tag"

        // TASK_TABLE
        const val TASK_TABLE = "goals"
        // TASK_TABLE ATTRIBUTES
        const val TASK_ID = "task_id"
        const val TASK_TITLE = "title"
        const val TASK_TIME_CREATED = "time_created"
        const val TASK_TIME_EXPECTED = "time_expected"
        const val TASK_TIME_COMPLETED = "time_completed"
        const val TASK_DESCRIPTION = "description"
        const val TASK_PRIORITY = "priority"
        const val TASK_STATE = "state"
        const val TASK_GOAL_ID = "goal_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_GOAL_TABLE = "CREATE TABLE IF NOT EXISTS $GOAL_TABLE" +
                "( $GOAL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                  "$GOAL_TITLE TEXT UNIQUE NOT NULL, " +
                  "$GOAL_TIME_CREATED TEXT UNIQUE NOT NULL, " +
                  "$GOAL_TIME_EXPECTED TEXT NOT NULL, " +
                  "$GOAL_TIME_COMPLETED TEXT NOT NULL, " +
                  "$GOAL_DESCRIPTION TEXT NOT NULL, " +
                  "$GOAL_PRIORITY TEXT NOT NULL, " +
                  "$GOAL_STATE TEXT NOT NULL, " +
                  "$GOAL_TAG TEXT NOT NULL)"

        val CREATE_TASK_TABLE = "CREATE TABLE IF NOT EXISTS $TASK_TABLE" +
                "( $TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$TASK_TITLE TEXT NOT NULL, " +
                "$TASK_TIME_CREATED TEXT NOT NULL, " +
                "$TASK_TIME_EXPECTED TEXT NOT NULL, " +
                "$TASK_TIME_COMPLETED TEXT NOT NULL, " +
                "$TASK_DESCRIPTION TEXT NOT NULL, " +
                "$TASK_PRIORITY TEXT NOT NULL, " +
                "$TASK_STATE TEXT NOT NULL, " +
                "$TASK_GOAL_ID INTEGER NOT NULL, " +
                "FOREIGN KEY($TASK_GOAL_ID) REFERENCES $GOAL_TABLE($GOAL_ID))"

        db?.execSQL(CREATE_GOAL_TABLE)
        db?.execSQL(CREATE_TASK_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $GOAL_TABLE")
        db!!.execSQL("DROP TABLE IF EXISTS $TASK_TABLE")
        onCreate(db)
    }


}