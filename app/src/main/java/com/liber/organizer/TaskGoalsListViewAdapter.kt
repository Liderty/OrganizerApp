package com.liber.organizer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.listview_rating_row.view.*

class TaskGoalsListViewAdapter (var listViewContext: Context, var resources: Int, var goalItems:MutableList<Goal>) : ArrayAdapter<Goal>(listViewContext, resources, goalItems) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(resources, null)

        view.goalCheckbox.setEnabled(false)
        view.goalContent.text = goalItems[position].goalContent

        if(goalItems[position].goalStatus == 1) {
            view.goalArea.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSuccessful))
            view.goalCheckbox.isChecked = true
        }

        view.goalArea.setOnClickListener {
            Toast.makeText(context, "Pos: ${position} Status: ${goalItems[position].goalStatus}", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
