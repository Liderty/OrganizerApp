package com.liber.organizer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_categories.view.*

class CategoriesFragment : Fragment() {

    lateinit var categorylistView: ListView
    lateinit var db: DataBaseHandler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        categorylistView = view.findViewById(R.id.categoryListView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = getContext()
        db = DataBaseHandler(context!!)

        var categoryList = db.readCategory()

        categorylistView.adapter = CategoryListViewAdapter(context, R.layout.listview_category_row, categoryList)
        categorylistView.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            //var intent = Intent(context, TaskActivity::class.java)
            //intent.putExtra("task", tasksList[position])
            //startActivity(intent)
            Toast.makeText(context, "You clicked ${position}", Toast.LENGTH_SHORT)
        }

        view.btnCreateCategroy.setOnClickListener {
            var intent = Intent(context, AddCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}

