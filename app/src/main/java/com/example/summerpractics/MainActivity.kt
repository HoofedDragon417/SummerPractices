package com.example.summerpractics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.summerpractics.storage.DataBaseHelper
import com.example.summerpractics.databinding.ActivityMainBinding
import com.example.summerpractics.fragments.MainFragment
import com.example.summerpractics.models.WeekDateSaveModel
import com.example.summerpractics.storage.SharedPreference
import java.util.*


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()


        val currentDates = DataBaseHelper(applicationContext).viewDates()

        var calendar = Calendar.getInstance()

        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        calendar = setZeros(calendar)
        val startPeriod = calendar.timeInMillis

        calendar[Calendar.WEEK_OF_MONTH] += 1
        val endPeriod = calendar.timeInMillis

        if (currentDates.id == 0) {

            val date = WeekDateSaveModel(0, startPeriod, endPeriod)

            DataBaseHelper(applicationContext).addDates(date)

        } else {

            if (startPeriod != currentDates.periodBegin) {

                val date = WeekDateSaveModel(currentDates.id, startPeriod, endPeriod)

                DataBaseHelper(applicationContext).updateDates(date)

            }

        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.test_fragment, MainFragment.newInstance()).commit()

    }

    private fun setZeros(date: Calendar): Calendar {

        date[Calendar.HOUR_OF_DAY] = 0
        date[Calendar.MINUTE] = 0
        date[Calendar.SECOND] = 0

        return date

    }

}