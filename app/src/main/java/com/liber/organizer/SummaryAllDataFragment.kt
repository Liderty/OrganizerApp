package com.liber.organizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_summary_all_data.*
import java.util.*
import kotlin.collections.ArrayList

class SummaryAllDataFragment : Fragment() {

    lateinit var db: DataBaseHandler
    val GRADE_NAMES = arrayListOf<String>("None","Bad","Not Good","Middle","Nice","Great")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_summary_all_data, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = getContext()
        db = DataBaseHandler(context!!)

        setUpPieChartData()
    }

    private fun setUpPieChartData() {
        val chartValues = setData()

        val dataSet = PieDataSet(chartValues, "")
        dataSet.valueTextSize = 0f

        val colors = java.util.ArrayList<Int>()
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
//        colors.add(ColorTemplate.getHoloBlue())
        dataSet.setColors(colors)

        val data = PieData(dataSet)
        chartAllGrades.data = data
        chartAllGrades.centerTextRadiusPercent = 0f
        chartAllGrades.isDrawHoleEnabled = false
        chartAllGrades.legend.isEnabled = false
        chartAllGrades.description.isEnabled = false
    }

    private fun setData() : ArrayList<PieEntry> {
        val chartData = ArrayList<PieEntry>()
        val allGrades = db.readGrades()
        val summary = arrayOf(0,0,0,0,0,0)

        for (grade in allGrades) {
            summary[grade.gradeGrade]++
        }

        var avarage = 0f
        for (element in summary) {
            avarage = avarage + element
        }

        for (i in 1..(summary.size - 1)) {
            if(summary[i] != 0) {
                chartData.add(PieEntry(summary[i] / avarage, GRADE_NAMES[i]))
            }
        }
        return chartData
    }

    override fun onResume() {
        super.onResume()
        setUpPieChartData()
    }
}
