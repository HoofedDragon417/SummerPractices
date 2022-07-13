package com.example.summerpractics

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.summerpractics.databinding.ActivityMainBinding
import com.example.summerpractics.fragments.MainFragment
import com.example.summerpractics.models.DatePeriodModel
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


        val currentDates = SharedPreference(applicationContext).getDate()

        var calendar = Calendar.getInstance()

        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        calendar = setZeros(calendar)
        val startPeriod = calendar.timeInMillis

        calendar[Calendar.WEEK_OF_MONTH] += 1
        val endPeriod = calendar.timeInMillis

        if (currentDates.periodBegin.toInt() == 0)
            SharedPreference(applicationContext).saveDate(DatePeriodModel(startPeriod, endPeriod))
        else
            if (startPeriod != currentDates.periodBegin)
                SharedPreference(applicationContext).saveDate(
                    DatePeriodModel(
                        startPeriod,
                        endPeriod
                    )
                )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, MainFragment.newInstance()).commit()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("test save", "testing life cycle(onSaveInstanceState) in activity")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("test save", "testing life cycle(onRestoreInstanceState) in activity")
    }

    private fun setZeros(date: Calendar): Calendar {

        date[Calendar.HOUR_OF_DAY] = 0
        date[Calendar.MINUTE] = 0
        date[Calendar.SECOND] = 0

        return date

    }

}