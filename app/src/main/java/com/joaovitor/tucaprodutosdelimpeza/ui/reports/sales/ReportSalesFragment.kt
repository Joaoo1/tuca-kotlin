package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportSalesBinding

class ReportSalesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentReportSalesBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_report_sales, container, false)



        return binding.root
    }
}