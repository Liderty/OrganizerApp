package com.liber.organizer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.ColorTemplate

class SummaryCategoryListViewAdapter (var listViewContext: Context, var resources: Int, var categoryItems:List<Category>) : ArrayAdapter<Category>(listViewContext, resources, categoryItems) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(listViewContext)
        val view: View = layoutInflater.inflate(resources, null)
        val db = DataBaseHandler(context!!)

        val categoryImageView: ImageView = view.findViewById(R.id.categoryImage)
        val categoryNameTextView: TextView = view.findViewById(R.id.categoryName)
        val barChart: BarChart = view.findViewById(R.id.barChart)

        val categoryItem: Category = categoryItems[position]
        categoryImageView.setImageDrawable(listViewContext.resources.getDrawable(categoryItem.categoryIcon))
        categoryNameTextView.text = categoryItem.categoryName

        val taskList = db.readTasks(categoryItem.categoryId)

        // create BarEntry for Bar Group
        val bargroup = ArrayList<BarEntry>()

        for (i in 0..taskList.size-1) {
            bargroup.add(BarEntry(taskList[i].taskAvarage.toFloat(), i.toFloat(), listViewContext.getDrawable((taskList[i].taskIcon))))
        }

        val barDataSet = BarDataSet(bargroup, "Categories")

        val colors = java.util.ArrayList<Int>()
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        barDataSet.setColors(colors)

        val data = BarData(barDataSet)
        barChart.setData(data)
//        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        barChart.xAxis.labelCount = 11
        barChart.xAxis.enableGridDashedLine(5f, 5f, 0f)

        barChart.setDrawGridBackground(false)
        barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
        barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.legend.isEnabled = true
        barChart.setPinchZoom(true)
//        barChart.data.setDrawValues(false)

        return view
    }
}