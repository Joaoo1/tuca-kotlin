package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportProductsSoldBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.reports.ReportsFragmentDirections

class ReportProductsSoldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = resources.getString(R.string.title_fragment_report_products_sold)
        val binding: FragmentReportProductsSoldBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_report_products_sold, container, false)
        binding.generate.setOnClickListener {
            this.findNavController()
                .navigate(ReportProductsSoldFragmentDirections.actionReportProductsSoldFragmentToProductsSoldListFragment())
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}