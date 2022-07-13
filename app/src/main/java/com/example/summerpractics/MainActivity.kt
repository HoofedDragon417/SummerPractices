package com.example.summerpractics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.summerpractics.databinding.ActivityMainBinding
import com.example.summerpractics.fragments.MainFragment
import com.example.summerpractics.models.DatePeriodModel
import com.example.summerpractics.storage.DataBaseHelper
import com.example.summerpractics.storage.SharedPreference
import java.util.*

const val AUTO_DELETE_CHECK = "auto-delete_check"

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        val dateCheckFirstStart = SharedPreference(applicationContext).getDate()
        val autoDeleteCheck = SharedPreference(applicationContext).getBoolean(AUTO_DELETE_CHECK)

        val currentDates = SharedPreference(applicationContext).getDate()

        var calendar = Calendar.getInstance()

        if ((calendar[Calendar.MONTH] == 6 || calendar[Calendar.MONTH] == 12) && !autoDeleteCheck) {
            var temporaryValue = calendar
            temporaryValue[Calendar.DAY_OF_MONTH] = 1
            temporaryValue = setZeros(temporaryValue)
            DataBaseHelper(applicationContext).deleteMeetings(temporaryValue.timeInMillis)
            DataBaseHelper(applicationContext).deleteTasks(temporaryValue.timeInMillis)

            SharedPreference(applicationContext).saveBoolean(AUTO_DELETE_CHECK, true)
        } else
            SharedPreference(applicationContext).saveBoolean(AUTO_DELETE_CHECK, false)

        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        calendar = setZeros(calendar)
        val startPeriod = calendar.timeInMillis

        calendar[Calendar.WEEK_OF_MONTH] += 1
        val endPeriod = calendar.timeInMillis

        if (currentDates.periodBegin == dateCheckFirstStart.periodBegin)
            SharedPreference(applicationContext).saveDate(DatePeriodModel(startPeriod, endPeriod))
        else
            if (startPeriod != currentDates.periodBegin)
                SharedPreference(applicationContext).saveDate(
                    DatePeriodModel(startPeriod, endPeriod)
                )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, MainFragment.newInstance()).commit()

    }

    private fun setZeros(date: Calendar): Calendar {

        date[Calendar.HOUR_OF_DAY] = 0
        date[Calendar.MINUTE] = 0
        date[Calendar.SECOND] = 0

        return date

    }

}