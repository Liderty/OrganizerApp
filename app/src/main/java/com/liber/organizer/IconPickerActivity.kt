package com.liber.organizer

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_icon_picker.*
import kotlinx.android.synthetic.main.icons_entry.view.*

class IconPickerActivity : AppCompatActivity() {

    var foodsList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_picker)
        val context = this

        foodsList.add(R.drawable.check)
        foodsList.add(R.drawable.business_contact)
        foodsList.add(R.drawable.contact)
        foodsList.add(R.drawable.cost)
        foodsList.add(R.drawable.digit_five)
        foodsList.add(R.drawable.digit_four)
        foodsList.add(R.drawable.digit_three)
        foodsList.add(R.drawable.digit_two)
        foodsList.add(R.drawable.digit_one)
        foodsList.add(R.drawable.edit)
        foodsList.add(R.drawable.future_projects)
        foodsList.add(R.drawable.lightbulb)
        foodsList.add(R.drawable.payment_card)

        val adapter = FoodAdapter(this, foodsList)
        gridViewIcons.adapter = adapter

        gridViewIcons.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent()
            intent.putExtra("klucz", foodsList[position])
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        buttonCancel.setOnClickListener {
            finish()
        }
    }

    class FoodAdapter : BaseAdapter {
        var foodsList = ArrayList<Int>()
        var context: Context? = null

        constructor(context: Context, foodsList: ArrayList<Int>) : super() {
            this.context = context
            this.foodsList = foodsList
        }

        override fun getCount(): Int {
            return foodsList.size
        }

        override fun getItem(position: Int): Any {
            return foodsList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val food = this.foodsList[position]

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var foodView = inflator.inflate(R.layout.icons_entry, null)
            foodView.imgFood.setImageResource(food)

            return foodView
        }
    }
}
