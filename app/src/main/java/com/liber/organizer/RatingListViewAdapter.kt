package com.liber.organizer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import kotlinx.android.synthetic.main.listview_rating_row.view.*

class RatingListViewAdapter (var listViewContext: Context, var resources: Int, var goalItems:MutableList<Goal>) : ArrayAdapter<Goal>(listViewContext, resources, goalItems) {


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(listViewContext)
        val view: View = layoutInflater.inflate(resources, null)
        val db = DataBaseHandler(context)

        view.goalContent.text = goalItems[position].goalContent

        view.goalCheckbox.setOnClickListener {
            db.updateGoalStatus(goalItems[position].goalId)
            goalItems.remove(goalItems[position])
            notifyDataSetChanged()
        }
        return view
    }
}

