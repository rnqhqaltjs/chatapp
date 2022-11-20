package com.example.chatapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.chatapp.R
import com.example.chatapp.data.db.MemoDatabase
import com.example.chatapp.data.model.Memo
import com.example.chatapp.data.repository.MemoRepository
import com.example.chatapp.util.CalendarDecorator.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 뷰모델은 DB에 직접 접근하지 않아야함. Repository 에서 데이터 통신.
class MemoViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData : LiveData<List<Memo>>
    private val repository : MemoRepository

    // get set
    private var _currentData = MutableLiveData<List<Memo>>()
    val currentData : LiveData<List<Memo>>
        get() = _currentData

    init{

        val memoDao = MemoDatabase.getDatabase(application)!!.memoDao()
        repository = MemoRepository(memoDao)
        readAllData = repository.readAllData.asLiveData()

    }

    fun addMemo(memo : Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMemo(memo)
        }
    }

    fun updateMemo(memo : Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMemo(memo)
        }
    }

    fun deleteMemo(memo : Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMemo(memo)
        }
    }

    fun readDateData(year : Int, month : Int, day : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tmp = repository.readDateData(year, month, day)
            _currentData.postValue(tmp)
        }
    }

    fun readMonthData(year : Int, month : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tmp = repository.readMonthData(year, month)
            _currentData.postValue(tmp)
        }
    }

    //calendar
    fun dotDecorator(context: Context, calendar: MaterialCalendarView?, memoDatabase: MemoDatabase) {
        val dates = ArrayList<CalendarDay>()

        viewModelScope.launch(Dispatchers.IO) {
            val scheduleList = memoDatabase.memoDao().getCalendarAll()
            if (scheduleList.isNotEmpty()) {
                for (i in scheduleList.indices) {
                    val dot_year = scheduleList[i].year
                    val dot_month = scheduleList[i].month
                    val dot_day = scheduleList[i].day

                    dates.add(CalendarDay(dot_year, dot_month, dot_day))
                }
            }

            withContext(Dispatchers.Main) {

                calendar!!.removeDecorators()
                calendar.invalidateDecorators()

                calendar.addDecorators(BoldDecorator(), SundayDecorator(), SaturdayDecorator(), MySelectorDecorator(context), TodayDecorator(context))
                if (dates.size > 0) {
                    calendar.addDecorator(EventDecorator(Color.BLACK, dates)) // 점 찍기

                }
            }
        }

    }


    fun TotalPieChart(memoDatabase: MemoDatabase, pieChart:PieChart,resources:Resources){

        val prices1 = ArrayList<Int>()
        val prices2 = ArrayList<Int>()

        viewModelScope.launch(Dispatchers.IO) {

            val value = memoDatabase.memoDao().getCalendarAll()
            if(value.isNotEmpty()){
                for (i in value.indices) {
                    val price = value[i].withdraw
                    val price2 = value[i].deposit
                    prices1.add(price)
                    prices2.add(price2)
                }
            }

            withContext(Dispatchers.Main) {

                pieChart.setUsePercentValues(true)
                pieChart.description.isEnabled = false
                pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

                // on below line we are setting drag for our pie chart
                pieChart.dragDecelerationFrictionCoef = 0.95f

                // on below line we are setting hole
                // and hole color for pie chart
                pieChart.isDrawHoleEnabled = false
                pieChart.setHoleColor(Color.WHITE)

                // on below line we are setting circle color and alpha
                pieChart.setTransparentCircleColor(Color.WHITE)
                pieChart.setTransparentCircleAlpha(110)

                // on  below line we are setting hole radius
                pieChart.holeRadius = 58f
                pieChart.transparentCircleRadius = 61f

                // on below line we are setting center text
                pieChart.setDrawCenterText(true)

                // on below line we are setting
                // rotation for our pie chart
                pieChart.rotationAngle = 0f

                // enable rotation of the pieChart by touch
                pieChart.isRotationEnabled = true
                pieChart.isHighlightPerTapEnabled = true

                // on below line we are setting animation for our pie chart
                pieChart.animateY(1400, Easing.EaseInOutQuad)

                // on below line we are disabling our legend for pie chart
                pieChart.legend.isEnabled = false
                pieChart.setEntryLabelColor(Color.WHITE)
                pieChart.setEntryLabelTextSize(15f)

                // on below line we are creating array list and
                // adding data to it to display in pie chart
                val entries: ArrayList<PieEntry> = ArrayList()
                entries.add(PieEntry(prices1.sum().toFloat(),"총지출"))
                entries.add(PieEntry(prices2.sum().toFloat(), "총수입"))

                // on below line we are setting pie data set
                val dataSet = PieDataSet(entries, "Mobile OS")

                // on below line we are setting icons.
                dataSet.setDrawIcons(false)

                // on below line we are setting slice for pie
                dataSet.sliceSpace = 3f
                dataSet.iconsOffset = MPPointF(0f, 40f)
                dataSet.selectionShift = 5f

                // add a lot of colors to list
                val colors: ArrayList<Int> = ArrayList()
                colors.add(resources.getColor(R.color.red))
                colors.add(resources.getColor(R.color.blue))

                // on below line we are setting colors.
                dataSet.colors = colors

                // on below line we are setting pie data set
                val data = PieData(dataSet)
                data.setValueFormatter(PercentFormatter(pieChart))
                data.setValueTextSize(25f)
                data.setValueTypeface(Typeface.DEFAULT_BOLD)
                data.setValueTextColor(Color.WHITE)
                pieChart.data = data

                // undo all highlights
                pieChart.highlightValues(null)

                // loading chart
                pieChart.invalidate()

            }

        }

    }
}