package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportSalesBinding
import com.joaovitor.tucaprodutosdelimpeza.util.DatePickerBuilder
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class  ReportSalesFragment : Fragment() {

    private lateinit var viewModel: ReportSalesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        val binding: FragmentReportSalesBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_report_sales, container, false)

        //Create the viewModel
        val viewModelFactory = ReportSalesViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)
            .get(ReportSalesViewModel::class.java)

        viewModel.openSelectAddressDialog.observe(viewLifecycleOwner){
            it?.let {
                showSelectAddressDialog(it.map { address ->  address.name }.toTypedArray()
                ) { _, i -> viewModel.onSelectAddress(it[i]) }
            }
        }

        viewModel.openDialog.observe(viewLifecycleOwner){
            if(it) showDialog()
        }

        viewModel.openStartDatePicker.observe(viewLifecycleOwner){
            if(it) {
                DatePickerBuilder.buildDatePicker({ millis ->
                    viewModel.onSelectStartDate(millis)
                }).show(parentFragmentManager, parentFragment.toString())
            }
        }

        viewModel.openEndDatePicker.observe(viewLifecycleOwner){
            if(it) {
                DatePickerBuilder.buildDatePicker({ millis ->
                        viewModel.onSelectEndDate(millis)
                    }).show(parentFragmentManager, parentFragment.toString())
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    private fun showSelectAddressDialog(items: Array<String>, clickListener: DialogInterface.OnClickListener) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.dialog_select_address_title))
                .setItems(items, clickListener)
                .setNegativeButton(getString(R.string.dialog_select_address_negative_button), null)
                .show()
        }
    }

    private fun showDialog() {
        val size = viewModel.filteredSales.value?.size.toString()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_print_sales_title)
            .setMessage(String.format(resources.getString(R.string.dialog_print_sales_text), size))
            .setPositiveButton(R.string.dialog_print_sales_visualize_button)
            { _, _ ->
                this.findNavController()
                    .navigate(ReportSalesFragmentDirections
                        .actionReportSalesFragmentToFilteredSalesFragment())

                viewModel.doneNavigating()
            }
            .setNegativeButton(R.string.dialog_print_sales_print_button) { _, _ -> viewModel.onClickPrintReport(requireContext())}
            .show()
    }
}