package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.content.DialogInterface
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportSalesBinding
import com.joaovitor.tucaprodutosdelimpeza.util.DatePickerBuilder

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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.openSelectAddressDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                showSelectAddressDialog(it.map { address ->  address.name }.toTypedArray(),
                    DialogInterface.OnClickListener { _, i ->
                    viewModel.onSelectAddress(it[i])})
            }
        })

        viewModel.openDialog.observe(viewLifecycleOwner, Observer {
            if(it) showDialog()
        })

        viewModel.openStartDatePicker.observe(viewLifecycleOwner, Observer {
            if(it) {
                DatePickerBuilder.buildDatePicker(
                    MaterialPickerOnPositiveButtonClickListener { millis ->
                    viewModel.onSelectStartDate(millis)
                }).show(parentFragmentManager, parentFragment.toString())
            }
        })

        viewModel.openEndDatePicker.observe(viewLifecycleOwner, Observer {
            if(it) {
                DatePickerBuilder.buildDatePicker(
                    MaterialPickerOnPositiveButtonClickListener { millis ->
                        viewModel.onSelectEndDate(millis)
                    }).show(parentFragmentManager, parentFragment.toString())
            }
        })

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
        val size = viewModel.filteredSales.value?.size
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_print_sales_title)
            .setMessage(R.string.dialog_print_sales_text)
            .setPositiveButton(String.format(resources.getString(R.string.dialog_print_sales_visualize_button), size.toString() ))
            { _, _ ->
                this.findNavController()
                    .navigate(ReportSalesFragmentDirections
                        .actionReportSalesFragmentToFilteredSalesFragment())

                viewModel.doneNavigating()
            }
            .setNegativeButton(R.string.dialog_print_sales_print_button, null)
            .show()
    }
}