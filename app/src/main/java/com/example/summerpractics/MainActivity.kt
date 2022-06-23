package com.example.summerpractics

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.summerpractics.database.DataBaseHelper
import com.example.summerpractics.databinding.ActivityMainBinding
import com.example.summerpractics.fragments.MainFragment
import com.example.summerpractics.models.WeekDateSaveModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        supportActionBar?.hide()

        val testList = DataBaseHelper(applicationContext).viewDates()

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.firstDayOfWeek = Calendar.MONDAY // Set the starting day of the week

        calendar.set(
            Calendar.DAY_OF_WEEK,
            Calendar.MONDAY
        ) // Pass whatever day you want to get inplace of `MONDAY`
        val startDate = calendar.time
        startDate.hours = 0
        startDate.minutes = 0
        startDate.seconds = 0

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val endDate = calendar.time
        endDate.hours = 0
        endDate.minutes = 0
        endDate.seconds = 0
        endDate.date += 1

        val startWeek = startDate.time
        val endWeek = endDate.time

        if (testList.size == 0) {

            val date = WeekDateSaveModel(0, startWeek, endWeek)

            DataBaseHelper(applicationContext).addDates(date)

        }

        if (testList.size != 0) {

            if (startWeek > testList[0].endWeek) {

                val date = WeekDateSaveModel(testList[0].id, startWeek, endWeek)

                Log.i("check data update", "date update check")

                DataBaseHelper(applicationContext).updateDates(date)

            }

        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.test_fragment, MainFragment.newInstance()).commit()

    }

}