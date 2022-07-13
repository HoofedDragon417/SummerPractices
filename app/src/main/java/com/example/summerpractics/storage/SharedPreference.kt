package com.example.summerpractics.storage

import android.content.Context
import com.example.summerpractics.models.DatePeriodModel

const val PREF_NAME = "theme"
const val DATE_PERIOD_START = "date_period_start"
const val DATE_PERIOD_END = "date_period_end"

class SharedPreference(ctx: Context) {

    private var data = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveInt(key: String, value: Int) {
        data.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return data.getInt(key, 0)
    }

    fun saveDate(date: DatePeriodModel) {
        data.edit().putLong(DATE_PERIOD_START, date.periodBegin).apply()
        data.edit().putLong(DATE_PERIOD_END, date.periodEnd).apply()
    }

    fun getDate(): DatePeriodModel {
        return DatePeriodModel(data.getLong(DATE_PERIOD_START, 0), data.getLong(DATE_PERIOD_END, 0))
    }

    fun saveBoolean(key: String, value: Boolean) {
        data.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return data.getBoolean(key,false)
    }
}