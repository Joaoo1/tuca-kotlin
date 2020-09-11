package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.content.Context
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportSalesBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class  ReportSalesFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var viewModel: ReportSalesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentReportSalesBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_report_sales, container, false)
        mContext = requireContext()
        val viewModelFactory = ReportSalesViewModelFactory()
        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ReportSalesViewModel::class.java)

        viewModel.openDialog.observe(viewLifecycleOwner, Observer {
            if(it) {
               showDialog()
            }
        })

        binding.filterSales.setOnClickListener{
            showSelectAddressDialog()
        }

        binding.startDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Selecione a data início")
            builder.setSelection(Calendar.getInstance().timeInMillis)
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale("pt-BR"))
                format.timeZone = TimeZone.getTimeZone("UTC")
                val selectedDate = format.format(Date(it))
                binding.startDate.setText(selectedDate)
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        binding.endDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Selecione a data final")
            builder.setSelection(Calendar.getInstance().timeInMillis)
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale("pt-BR"))
                format.timeZone = TimeZone.getTimeZone("UTC")
                val selectedDate = format.format(Date(it))
                binding.endDate.setText(selectedDate)
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        return binding.root
    }

    private fun showSelectAddressDialog() {
        val items = arrayOf("Teste", "Teste1", "teste2")
        val clickListener: DialogInterface.OnClickListener =
            DialogInterface.OnClickListener { _, i ->
                viewModel.onButtonFilterClicked()
            }
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Selecione um item")
                .setItems(items, clickListener)
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(mContext)
            .setTitle(R.string.dialog_print_sales_title)
            .setMessage(R.string.dialog_print_sales_text)
            .setPositiveButton(R.string.dialog_print_sales_visualize_button)
            { _, _ ->
                this.findNavController()
                    .navigate(ReportSalesFragmentDirections.actionReportSalesFragmentToFilteredSalesFragment())

                viewModel.doneNavigation()
            }
            .setNegativeButton(R.string.dialog_print_sales_print_button, null)
            .show()
    }
}