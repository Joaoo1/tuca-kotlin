package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.app.Dialog
import android.content.DialogInterface
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogFilterSalesBinding
import com.joaovitor.tucaprodutosdelimpeza.util.DatePickerBuilder
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate

class FilterSalesDialog(parentFragment: Fragment): Dialog(parentFragment.requireContext()) {

    private var viewModel: SaleListViewModel

    init {
        //Create the viewModel
        val viewModelFactory = SaleListViewModelFactory()
        viewModel = ViewModelProvider(parentFragment,viewModelFactory)
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

        viewModel.startDate.observe(parentFragment, Observer {
            if(it != null){
                binding.startDate.setText(FormatDate.formatDateToString(it))
            } else {
                binding.startDate.setText("")
            }
        })

        viewModel.endDate.observe(parentFragment, Observer {
            if(it != null){
                binding.endDate.setText(FormatDate.formatDateToString(it))
            } else {
                binding.endDate.setText("")
            }
        })

        viewModel.closeDialog.observe(parentFragment, Observer {
             if(it){
                 dismiss()
             }
        })

        setOnDismissListener {
            viewModel.doneDialogClosing()
        }
    }
}