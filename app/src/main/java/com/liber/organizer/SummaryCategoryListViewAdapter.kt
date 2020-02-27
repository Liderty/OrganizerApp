package com.liber.organizer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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
        val taskNames = ArrayList<String>()
        val bargroup = ArrayList<BarEntry>()

        for (i in 0..taskList.size-1) {
            bargroup.add(BarEntry(i.toFloat(), taskList[i].taskAvarage.toFloat(), resize(listViewContext.getDrawable((taskList[i].taskIcon))!!)))
            taskNames.add(taskList[i].taskName)
        }

        val barDataSet = BarDataSet(bargroup, "Tasks in category")

        val colors = java.util.ArrayList<Int>()
        for (c in ColorTemplate.MATERIAL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        barDataSet.setColors(colors)

        val data = BarData(barDataSet)
        data.setDrawValues(false)
        barChart.setData(data)

        val xAxis: XAxis = barChart.getXAxis()
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.TOP
        barChart.getXAxis().setValueFormatter(IndexAxisValueFormatter(taskNames))

        val yAxisLeft: YAxis = barChart.getAxisLeft()
        yAxisLeft.setAxisMinimum(0f)
        yAxisLeft.setAxisMaximum(6f)
        yAxisLeft.setGranularity(1f)

        val yAxisRight: YAxis = barChart.getAxisRight()
        yAxisRight.setAxisMinimum(0f)
        yAxisRight.setAxisMaximum(6f)
        yAxisRight.setGranularity(1f)

        barChart.setDrawGridBackground(false)
        barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
        barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.legend.isEnabled = true
        barChart.setPinchZoom(true)

        return view
    }

    private fun resize(image: Drawable): Drawable? {
        val b = (image as BitmapDrawable).bitmap
        val bitmapResized = Bitmap.createScaledBitmap(b, 32, 32, false)
        return BitmapDrawable(context.getResources(), bitmapResized)
    }
}