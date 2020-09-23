package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportProductsSoldBinding
import com.joaovitor.tucaprodutosdelimpeza.util.DatePickerBuilder

class ReportProductsSoldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentReportProductsSoldBinding =  DataBindingUtil.inflate(
            inflater,R.layout.fragment_report_products_sold, container, false)

        //Create the viewModel
        val viewModelFactory = ReportProductsSoldViewModelFactory()
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ReportProductsSoldViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.openStartDatePicker.observe(viewLifecycleOwner, Observer {
            if(it) {
                DatePickerBuilder.buildDatePicker(
                    MaterialPickerOnPositiveButtonClickListener { millis ->
                        viewModel.onSelectStartDate(millis)
                }).show(parentFragmentManager, parentFragment.toString())
            }
        })

        viewModel.openEndDatePicker.observe(viewLifecycleOwner, Observer {
            if(it){
                DatePickerBuilder.buildDatePicker(
                    MaterialPickerOnPositiveButtonClickListener { millis ->
                        viewModel.onSelectEndDate(millis)
                }).show(parentFragmentManager, parentFragment.toString())
            }
        })

        viewModel.navigateToProductsSoldList.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController()
                    .navigate(ReportProductsSoldFragmentDirections
                        .actionReportProductsSoldFragmentToProductsSoldListFragment())
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }
}