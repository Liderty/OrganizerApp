package com.liber.organizer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import kotlinx.android.synthetic.main.fragment_summary_categories.*

class SummaryCategoriesFragment : Fragment() {

    lateinit var db: DataBaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summary_categories, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DataBaseHandler(context!!)

        var categoryList = db.readCategory()
    }

    override fun onResume() {
        super.onResume()
        val context = getContext()

        var categoryList = db.readCategory()
        listViewCategoriesCharts.adapter = SummaryCategoryListViewAdapter(context!!, R.layout.listview_summary_categories_row, categoryList)
    }
}
