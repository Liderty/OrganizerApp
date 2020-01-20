package com.liber.organizer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class RatingListViewAdapter(
    var listViewContext: Context,
    var resources: Int,
    var gradeItems: List<Grade>
) : ArrayAdapter<Grade>(listViewContext, resources, gradeItems) {

    var db = DataBaseHandler(context)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(listViewContext)
        val view: View = layoutInflater.inflate(resources, null)

        val taskImageView: ImageView = view.findViewById(R.id.taskImage)
        val taskTitleTextView: TextView = view.findViewById(R.id.taskTitle)
        val taskDescriptionTextView: TextView = view.findViewById(R.id.taskDescription)
        val taskGradeTextView: TextView = view.findViewById(R.id.taskGrade)
        val taskRatingBar: RatingBar = view.findViewById(R.id.taskRatingBar)
        val taskRateButton: Button = view.findViewById(R.id.btnRate)

        val gradeItem: Grade = gradeItems[position]
        val taskItem: Task = db.readTask(gradeItem.taskId)

        taskImageView.setImageDrawable(listViewContext.resources.getDrawable(taskItem.taskIcon))
        taskTitleTextView.text = taskItem.taskName
        taskDescriptionTextView.text = taskItem.taskDescription

        if (taskItem.taskAvarage != 0.toDouble()) {
            taskGradeTextView.text = taskItem.taskAvarage.toString()
        } else {
            taskGradeTextView.text = ""
        }

        taskRateButton.setOnClickListener {
            println("tid: ${taskItem.taskId} gid: ${gradeItem.gradeId} rat: ${taskRatingBar.rating.toInt()}")
            db.updateGradeGrade(gradeItem.gradeId, taskRatingBar.rating.toInt())
            reloadData(gradeItem)
        }

        return view
    }

    fun reloadData(gradeItem: Grade) {
        this.remove(gradeItem)
        this.notifyDataSetChanged()
    }

}
