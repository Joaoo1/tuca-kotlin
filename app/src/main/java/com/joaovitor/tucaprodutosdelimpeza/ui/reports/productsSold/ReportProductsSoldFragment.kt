package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportProductsSoldBinding
import com.joaovitor.tucaprodutosdelimpeza.util.DatePickerBuilder
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

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
        val viewModel = ViewModelProvider(requireActivity(),viewModelFactory)
            .get(ReportProductsSoldViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.openStartDatePicker.observe(viewLifecycleOwner) {
            if(it) {
                DatePickerBuilder.buildDatePicker({ millis -> viewModel.onSelectStartDate(millis) })
                    .show(parentFragmentManager, parentFragment.toString())
            }
        }

        viewModel.openEndDatePicker.observe(viewLifecycleOwner) {
            if(it){
                DatePickerBuilder.buildDatePicker({ millis -> viewModel.onSelectEndDate(millis) })
                    .show(parentFragmentManager, parentFragment.toString())
            }
        }

        viewModel.navigateToProductsSoldList.observe(viewLifecycleOwner) {
            if(it) {
                findNavController()
                    .navigate(ReportProductsSoldFragmentDirections
                        .actionReportProductsSoldFragmentToProductsSoldListFragment())
                viewModel.doneNavigating()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                context?.toastLong(it)
                viewModel.doneShowError()
            }
        }

        viewModel.info.observe(viewLifecycleOwner) {
            it?.let {
                context?.toast(it)
                viewModel.doneShowInfo()
            }
        }

        viewModel.showProgressBar.observe(viewLifecycleOwner) {
            if(it) {
                (activity as MainActivity).showProgressBar()
            } else {
                (activity as MainActivity).hideProgressBar()
            }
        }

        return binding.root
    }
}