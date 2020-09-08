package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaovitor.tucaprodutosdelimpeza.R

class ProductsSoldListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = resources.getString(R.string.title_fragment_report_products_sold_list)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_sold_list, container, false)
    }
}