package com.liber.organizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainTabAdapter = MainViewPagerAdapter(supportFragmentManager)
        mainTabAdapter.addFragment(CategoriesFragment(), "Categories")
        mainTabAdapter.addFragment(SummaryFragment(), "Summary")
        mainViewPager.adapter = mainTabAdapter
        mainTabs.setupWithViewPager(mainViewPager)
    }

    class MainViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter (manager) {
        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val titleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String){
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }
}
