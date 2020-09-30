package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.app.Dialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogFilterSalesBinding
import com.joaovitor.tucaprodutosdelimpeza.util.DatePickerBuilder

class FilterSalesDialog(parentFragment: Fragment): Dialog(parentFragment.requireContext()) {

    init {
        //Create the viewModel
        val viewModelFactory = SaleListViewModelFactory()
        val viewModel: SaleListViewModel = ViewModelProvider(parentFragment,viewModelFactory)
            .get(SaleListViewModel::class.java)

        // Inflate the layout for this fragment
        val binding: DialogFilterSalesBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.dialog_filter_sales, null, false)

        setContentView(binding.root)
        binding.lifecycleOwner = parentFragment
        binding.viewModel = viewModel

        binding.startDate.setOnClickListener {
            DatePickerBuilder.buildDatePicker(MaterialPickerOnPositiveButtonClickListener {
                viewModel.selectedStartDate(it)
            }).show(parentFragment.parentFragmentManager, this.toString())
        }

        binding.endDate.setOnClickListener {
            DatePickerBuilder.buildDatePicker(MaterialPickerOnPositiveButtonClickListener {
                viewModel.selectedEndDate(it)
            }).show(parentFragment.parentFragmentManager, this.toString())
        }

        viewModel.closeFiltersDialog.observe(parentFragment, Observer {
            if(it) {
                dismiss()
            }
        })
    }
}