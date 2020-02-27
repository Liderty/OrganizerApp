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
import kotlinx.android.synthetic.main.activity_icon_picker.*
import kotlinx.android.synthetic.main.icons_entry.view.*

class IconPickerActivity : AppCompatActivity() {

    var foodsList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_picker)
        val context = this

        foodsList.add(R.drawable.airplane)
        foodsList.add(R.drawable.baggage)
        foodsList.add(R.drawable.ball)
        foodsList.add(R.drawable.barbecue)
        foodsList.add(R.drawable.beach)
        foodsList.add(R.drawable.bed)
        foodsList.add(R.drawable.beer)
        foodsList.add(R.drawable.bicycle)
        foodsList.add(R.drawable.bowling)
        foodsList.add(R.drawable.burger)
        foodsList.add(R.drawable.cakes)
        foodsList.add(R.drawable.clock)
        foodsList.add(R.drawable.clock2)
        foodsList.add(R.drawable.coffe)
        foodsList.add(R.drawable.compact)
        foodsList.add(R.drawable.dancer)
        foodsList.add(R.drawable.directional)
        foodsList.add(R.drawable.headphones)
        foodsList.add(R.drawable.hearts)
        foodsList.add(R.drawable.money)
        foodsList.add(R.drawable.multiple_stars)
        foodsList.add(R.drawable.restaurant)
        foodsList.add(R.drawable.running)
        foodsList.add(R.drawable.shower)
        foodsList.add(R.drawable.smoke)
        foodsList.add(R.drawable.star)
        foodsList.add(R.drawable.tennis)

        foodsList.add(R.drawable.brush)
        foodsList.add(R.drawable.elevator)
        foodsList.add(R.drawable.food)
        foodsList.add(R.drawable.garage)
        foodsList.add(R.drawable.glass)
        foodsList.add(R.drawable.jacuzzi)
        foodsList.add(R.drawable.love)
        foodsList.add(R.drawable.mastercard)
        foodsList.add(R.drawable.pass)
        foodsList.add(R.drawable.plate)
        foodsList.add(R.drawable.rss)
        foodsList.add(R.drawable.silhouette)
        foodsList.add(R.drawable.sport)
        foodsList.add(R.drawable.swimming)
        foodsList.add(R.drawable.taxi)
        foodsList.add(R.drawable.telephone)


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
