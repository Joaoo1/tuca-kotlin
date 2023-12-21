package com.joaovitor.tucaprodutosdelimpeza.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportsBinding

class ReportsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentReportsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reports, container, false)

        navigateOnClick(binding.reportProductsSold, ReportsFragmentDirections.actionReportsFragmentToReportProductsSoldFragment())
        navigateOnClick(binding.reportSales, ReportsFragmentDirections.actionReportsFragmentToReportSalesFragment())
        navigateOnClick(binding.reportSeller, ReportsFragmentDirections.actionReportsFragmentToReportSellerFragment())

        return binding.root
    }

    private fun navigateOnClick(view: View, directions: NavDirections) {
        view.setOnClickListener{
            this.findNavController().navigate(directions)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(4)?.isChecked = true
    }
}


