package com.liber.organizer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class TaskListViewAdapter(
    var listViewContext: Context,
    var resources: Int,
    var taskItems: List<Task>
) : ArrayAdapter<Task>(listViewContext, resources, taskItems) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(listViewContext)
        val view: View = layoutInflater.inflate(resources, null)

        val taskImageView: ImageView = view.findViewById(R.id.taskImage)
        val taskTitleTextView: TextView = view.findViewById(R.id.taskTitle)
        val taskDescriptionTextView: TextView = view.findViewById(R.id.taskDescription)
        val taskGradeRatingBar: RatingBar = view.findViewById(R.id.taskGrade)

        val taskItem = taskItems[position]

        taskImageView.setImageDrawable(listViewContext.resources.getDrawable(taskItem.taskIcon))
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription

        if (taskItem.taskAvarage != 0.toDouble()) {
            taskGradeRatingBar.rating = taskItem.taskAvarage.toFloat()

        } else {
            taskGradeRatingBar.visibility = View.INVISIBLE
        }

        return view
    }
}
