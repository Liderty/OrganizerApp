package com.liber.organizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val manger = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainTabAdapter = FragmentViewPagerAdapter(manger)

        mainTabAdapter.addFragment(CategoriesFragment(), "Categories")
        mainTabAdapter.addFragment(SummaryFragment(), "Summary")

        mainViewPager.adapter = mainTabAdapter
        mainTabs.setupWithViewPager(mainViewPager)
    }
}
