package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportProductsSoldBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.reports.ReportsFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class ReportProductsSoldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = resources.getString(R.string.title_fragment_report_products_sold)

        // Inflate the layout for this fragment
        val binding: FragmentReportProductsSoldBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_report_products_sold, container, false)
        binding.generate.setOnClickListener {
            this.findNavController()
                .navigate(ReportProductsSoldFragmentDirections.actionReportProductsSoldFragmentToProductsSoldListFragment())
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
}