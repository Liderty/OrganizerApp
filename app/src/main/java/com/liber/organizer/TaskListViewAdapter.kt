package com.liber.organizer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.liber.organizer.R

class TaskListViewAdapter (var listViewContext: Context, var resources: Int, var taskItems:List<Task>):ArrayAdapter<Task>(listViewContext, resources, taskItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(listViewContext)
        val view: View = layoutInflater.inflate(resources, null)

        val taskImageView: ImageView = view.findViewById(R.id.taskImage)
        val taskTitleTextView: TextView = view.findViewById(R.id.taskTitle)
        val taskDescriptionTextView: TextView = view.findViewById(R.id.taskDescription)
        val taskGradeTextView: TextView = view.findViewById(R.id.taskGrade)

        var taskItem:Task = taskItems[position]
        taskImageView.setImageDrawable(listViewContext.resources.getDrawable(taskItem.taskIcon))
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription
        if(taskItem.taskAvarage != 0.toDouble()) {
            taskGradeTextView.text = taskItem.taskAvarage.toString()
        } else {
            taskGradeTextView.text = ""
        }


        return view
    }

}