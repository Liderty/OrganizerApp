package com.liber.organizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.DialogFragment

class RatingDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var db = DataBaseHandler(this.context!!)
        val context = getContext()

        var emptyGrades = db.readEmptyGrades()

        val rootView = inflater.inflate(R.layout.dialog_rating_list, container)
        val ratingListView = rootView.findViewById(R.id.listviewRatings) as ListView

        ratingListView.adapter = RatingListViewAdapter(context!!, R.layout.listview_rating_row, emptyGrades)
        return rootView
    }
}