package com.liber.organizer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CategoryListViewAdapter (var listViewContext: Context, var resources: Int, var categoryItems:List<Category>) : ArrayAdapter<Category>(listViewContext, resources, categoryItems) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(listViewContext)
        val view: View = layoutInflater.inflate(resources, null)

        val categoryImageView: ImageView = view.findViewById(R.id.categoryImage)
        val categoryNameTextView: TextView = view.findViewById(R.id.categoryName)

        val categoryItem: Category = categoryItems[position]
        categoryImageView.setImageDrawable(listViewContext.resources.getDrawable(categoryItem.categoryIcon))
        categoryNameTextView.text = categoryItem.categoryName

        return view
    }
}
