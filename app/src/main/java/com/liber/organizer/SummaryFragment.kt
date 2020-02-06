package com.liber.organizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_summary.*

class SummaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summary, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = getContext()

        var fragmentsArray = ArrayList<Fragment>()

        fragmentsArray.add(SummaryAllDataFragment())
        fragmentsArray.add(SummaryCategoriesFragment())

        var adapter = ArrayAdapter(
            context!!,
            R.layout.spinner_summary,
            resources.getStringArray(R.array.summaryFragments)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        summarySpinner.adapter = adapter
        summarySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setFragment(fragmentsArray[position])
            }
        }
    }

    fun setFragment(fragment: Fragment) {
        var transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.summaryFrame, fragment)
        transaction.commit()
    }
}