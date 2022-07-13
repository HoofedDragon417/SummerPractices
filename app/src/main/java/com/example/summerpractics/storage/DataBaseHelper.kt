package com.example.summerpractics.storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.summerpractics.models.DatePeriodModel
import com.example.summerpractics.models.MeetingDataModel
import com.example.summerpractics.models.TaskDataModel

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "MeetingsAndTasks"
        private const val DATABASE_VERSION = 1
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_NOTE = "note"

        private const val TABLE_NAME_TASKS = "Tasks"
        private const val KEY_DURATION = "duration"
        private const val KEY_PRIORITY = "priority"
        private const val KEY_COMPLETED = "completed"
        private const val KEY_FOR_DELETE = "time_of_creation"

        private const val TABLE_NAME_MEETINGS = "Meetings"
        private const val KEY_TIME_BEGIN = "timeBegin"
        private const val KEY_TIME_END = "timeEnd"
    }

    private val CREATE_TABLE_TASKS = "create table $TABLE_NAME_TASKS(" +
            "$KEY_ID integer primary key autoincrement," +
            "$KEY_TITLE text," +
            "$KEY_NOTE text," +
            "$KEY_DURATION real," +
            "$KEY_PRIORITY integer," +
            "$KEY_COMPLETED integer," +
            "$KEY_FOR_DELETE real" +
            ")"

    private val CREATE_TABLE_MEETING = "create table $TABLE_NAME_MEETINGS(" +
            "$KEY_ID integer primary key autoincrement," +
            "$KEY_TITLE text," +
            "$KEY_NOTE text," +
            "$KEY_TIME_BEGIN real," +
            "$KEY_TIME_END real" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_TASKS)
        db?.execSQL(CREATE_TABLE_MEETING)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Бесполезно(пока)
    }

    fun addMeeting(meeting: MeetingDataModel) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(KEY_TITLE, meeting.title)
        values.put(KEY_NOTE, meeting.note)
        values.put(KEY_TIME_BEGIN, meeting.beginTime)
        values.put(KEY_TIME_END, meeting.endTime)

        db.insert(TABLE_NAME_MEETINGS, null, values)
        db.close()
    }

    fun viewMeetings(dates: DatePeriodModel): ArrayList<MeetingDataModel> {
        val listOfMeetings = ArrayList<MeetingDataModel>()

        val db = this.readableDatabase
        val query =
            "select * from $TABLE_NAME_MEETINGS " +
                    "where $KEY_TIME_BEGIN between ${dates.periodBegin} and ${dates.periodEnd}" +
                    " order by $KEY_TIME_BEGIN"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val note = cursor.getString(2)
                val timeBegin = cursor.getLong(3)
                val timeEnd = cursor.getLong(4)

                val meeting = MeetingDataModel(id, title, note, timeBegin, timeEnd)

                listOfMeetings.add(meeting)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return listOfMeetings
    }

    fun deleteMeetings(border: Long) {
        val db = this.writableDatabase

        db.delete(TABLE_NAME_MEETINGS, "$KEY_TIME_BEGIN <= $border and $KEY_COMPLETED = 1", null)
    }

    fun addTask(task: TaskDataModel) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(KEY_TITLE, task.title)
        values.put(KEY_NOTE, task.note)
        values.put(KEY_DURATION, task.duration)
        values.put(KEY_PRIORITY, task.priority)
        values.put(KEY_COMPLETED, task.completed)

        db.insert(TABLE_NAME_TASKS, null, values)
        db.close()
    }

    fun viewTasks(): ArrayList<TaskDataModel> {
        val listOfTasks = ArrayList<TaskDataModel>()

        val db = this.readableDatabase
        val query = "select * from $TABLE_NAME_TASKS " +
                "where $KEY_COMPLETED like 0 " +
                "order by $KEY_PRIORITY"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val note = cursor.getString(2)
                val duration = cursor.getDouble(3)
                val priority = cursor.getInt(4)
                val completed = cursor.getInt(5)
                val timeOfCreation = cursor.getLong(6)

                val task =
                    TaskDataModel(id, title, note, duration, priority, completed, timeOfCreation)

                listOfTasks.add(task)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return listOfTasks
    }

    fun updateTasks(task: TaskDataModel) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(KEY_TITLE, task.title)
        values.put(KEY_NOTE, task.note)
        values.put(KEY_DURATION, task.duration)
        values.put(KEY_PRIORITY, task.priority)
        values.put(KEY_COMPLETED, task.completed)

        db.update(TABLE_NAME_TASKS, values, "$KEY_ID = ${task.id}", null)
    }

    fun deleteTasks(border: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME_TASKS, "$KEY_FOR_DELETE < $border", null)
    }
}