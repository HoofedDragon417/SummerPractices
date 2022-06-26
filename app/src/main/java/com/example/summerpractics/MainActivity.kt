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
        setContentView(binding.root)

        supportActionBar?.hide()

        val testList = DataBaseHelper(applicationContext).viewDates()

        val calendar = Calendar.getInstance()

        var startDate = calendar
        val endDate = calendar

        startDate[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        startDate = setZeros(startDate)
        val startWeek = startDate.timeInMillis

        endDate[Calendar.WEEK_OF_MONTH] += 1
        val endWeek = endDate.timeInMillis

        if (testList.id == 0) {

            val date = WeekDateSaveModel(0, startWeek, endWeek)

            DataBaseHelper(applicationContext).addDates(date)

        } else {

            if (startWeek != testList.beginThisWeek) {

                val date = WeekDateSaveModel(testList.id, startWeek, endWeek)

                Log.i("check data update", "date update check")

                DataBaseHelper(applicationContext).updateDates(date)

            }

        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.test_fragment, MainFragment.newInstance()).commit()

    }

    fun setZeros(date: Calendar): Calendar {

        date[Calendar.HOUR_OF_DAY] = 0
        date[Calendar.MINUTE] = 0
        date[Calendar.SECOND] = 0

        return date

    }

}