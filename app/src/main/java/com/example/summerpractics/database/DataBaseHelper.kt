package com.example.summerpractics.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.summerpractics.models.MeetingDataModel
import com.example.summerpractics.models.TaskDataModel
import com.example.summerpractics.models.WeekDateSaveModel

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {

        private const val DATABASE_NAME = "MeetingsAndTasks"
        private const val DATABASE_VERSION = 1
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_NOTE = "note"

        const val TABLE_NAME_DATES = "Dates"
        private const val KEY_MONDAY = "monday"
        private const val KEY_SUNDAY = "sunday"

        private const val TABLE_NAME_TASKS = "Tasks"
        private const val KEY_DURATION = "duration"
        private const val KEY_PRIORITY = "priority"
        private const val KEY_COMPLETED = "completed"

        private const val TABLE_NAME_MEETINGS = "Meetings"
        private const val KEY_TIME_BEGIN = "timeBegin"
        private const val KEY_TIME_END = "timeEnd"

    }

    private val CREATE_TABLE_DATES = "create table $TABLE_NAME_DATES(" +
            "$KEY_ID integer primary key autoincrement," +
            "$KEY_MONDAY real," +
            "$KEY_SUNDAY real" +
            ")"

    private val CREATE_TABLE_TASKS = "create table $TABLE_NAME_TASKS(" +
            "$KEY_ID integer primary key autoincrement," +
            "$KEY_TITLE text," +
            "$KEY_NOTE text," +
            "$KEY_DURATION real," +
            "$KEY_PRIORITY integer," +
            "$KEY_COMPLETED integer" +
            ")"

    private val CREATE_TABLE_MEETING = "create table $TABLE_NAME_MEETINGS(" +
            "$KEY_ID integer primary key autoincrement," +
            "$KEY_TITLE text," +
            "$KEY_NOTE text," +
            "$KEY_TIME_BEGIN real," +
            "$KEY_TIME_END real" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(CREATE_TABLE_DATES)
        db?.execSQL(CREATE_TABLE_TASKS)
        db?.execSQL(CREATE_TABLE_MEETING)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME_DATES")
        db?.execSQL("drop table if exists $TABLE_NAME_MEETINGS")
        db?.execSQL("drop table if exists $TABLE_NAME_TASKS")
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

    fun viewMeetings(): ArrayList<MeetingDataModel> {

        val listOfMeetings = ArrayList<MeetingDataModel>()
        val listOfDates = this.viewDates()

        val db = this.readableDatabase
        val selectQuery =
            "select * from $TABLE_NAME_MEETINGS " +
                    "where $KEY_TIME_BEGIN between ${listOfDates[0].startWeek} and " +
                    "${listOfDates[0].endWeek} order by $KEY_TIME_BEGIN"

        val cursor = db.rawQuery(selectQuery, null)

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

    fun addDates(dates: WeekDateSaveModel) {

        val db = this.writableDatabase

        val values = ContentValues()

        values.put(KEY_MONDAY, dates.startWeek)
        values.put(KEY_SUNDAY, dates.endWeek)

        db.insert(TABLE_NAME_DATES, null, values)
        db.close()

    }

    fun viewDates(): ArrayList<WeekDateSaveModel> {

        val listOfDates = ArrayList<WeekDateSaveModel>()

        val db = this.readableDatabase
        val query = "select * from $TABLE_NAME_DATES"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                val id = cursor.getInt(0)
                val monday = cursor.getLong(1)
                val sunday = cursor.getLong(2)

                val dates = WeekDateSaveModel(id, monday, sunday)

                listOfDates.add(dates)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return listOfDates
    }

    fun updateDates(date: WeekDateSaveModel) {

        val values = ContentValues()

        values.put(KEY_MONDAY, date.startWeek)
        values.put(KEY_SUNDAY, date.endWeek)

        val db = this.writableDatabase

        db.update(
            TABLE_NAME_DATES,
            values, "$KEY_ID = ${date.id}", null
        )

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

                val task = TaskDataModel(id, title, note, duration, priority, completed)

                listOfTasks.add(task)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return listOfTasks

    }

}