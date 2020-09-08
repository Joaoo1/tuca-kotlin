package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.joaovitor.tucaprodutosdelimpeza.R

class FilteredSalesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_fragment_report_filtered_sales)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtered_sales, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.print_report, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}