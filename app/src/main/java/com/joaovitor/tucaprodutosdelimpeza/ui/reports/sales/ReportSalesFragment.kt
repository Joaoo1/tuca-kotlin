package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportSalesBinding

class  ReportSalesFragment : Fragment() {

    private lateinit var mContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = resources.getString(R.string.title_fragment_report_sales)

        val binding: FragmentReportSalesBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_report_sales, container, false)
        mContext = requireContext()
        val viewModelFactory = ReportSalesViewModelFactory()
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ReportSalesViewModel::class.java)

        viewModel.openDialog.observe(viewLifecycleOwner, Observer {
            if(it) {
               showDialog(viewModel)
            }
        })

        binding.filterSales.setOnClickListener{
            viewModel.onButtonFilterClicked()
        }

        return binding.root
    }

    private fun showDialog(viewModel: ReportSalesViewModel) {
        MaterialAlertDialogBuilder(mContext)
            .setTitle(R.string.dialog_print_sales_title)
            .setMessage(R.string.dialog_print_sales_text)
            .setPositiveButton(R.string.dialog_print_sales_visualize_button)
            { dialog, which ->
                this.findNavController()
                    .navigate(ReportSalesFragmentDirections.actionReportSalesFragmentToFilteredSalesFragment())

                viewModel.doneNavigation()
            }
            .setNegativeButton(R.string.dialog_print_sales_print_button, null)
            .show()
    }
}