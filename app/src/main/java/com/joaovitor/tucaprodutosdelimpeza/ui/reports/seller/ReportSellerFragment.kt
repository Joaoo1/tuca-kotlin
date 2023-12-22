package com.joaovitor.tucaprodutosdelimpeza.ui.reports.seller

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.User
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportSellerBinding
import com.joaovitor.tucaprodutosdelimpeza.util.DatePickerBuilder
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong
import java.util.Date

const val SELLER_SPINNER_HINT = "Selecione um vendedor"

class ReportSellerFragment : Fragment() {
    private val mediatorLiveData = MediatorLiveData<Triple<User?, Date?, Date?>>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentReportSellerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_report_seller, container, false
        )

        binding.viewModel = createViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        setupToasts(binding)
        setupProgressBar(binding)
        setSpinnerOnItemSelectedListener(binding)
        fillSpinnerOnSellersChange(binding)
        onlyEnableFilterButtonAfterAllFilledUp(binding)
        datePickersObservers(binding)
        navigateOnFilteredSales(binding)

        return binding.root
    }

    private fun navigateOnFilteredSales(binding: FragmentReportSellerBinding) {
        binding.viewModel!!.navigateToFilteredSales.observe(viewLifecycleOwner){
            if(it) {
                this.findNavController()
                    .navigate(
                        ReportSellerFragmentDirections
                        .actionReportSellerFragmentToFilteredSalesFragment())

                binding.viewModel!!.doneNavigating()
            }
        }
    }

    private fun setupProgressBar(binding: FragmentReportSellerBinding) {
        binding.viewModel!!.showProgressBar.observe(viewLifecycleOwner) {
            if(it) {
                (activity as MainActivity).showProgressBar()
            } else {
                (activity as MainActivity).hideProgressBar()
            }
        }
    }

    private fun setupToasts(binding: FragmentReportSellerBinding) {
        binding.viewModel!!.error.observe(viewLifecycleOwner) {
            it?.let {
                context?.toastLong(it)
                binding.viewModel!!.doneShowError()
            }
        }

        binding.viewModel!!.info.observe(viewLifecycleOwner) {
            it?.let {
                context?.toast(it)
                binding.viewModel!!.doneShowInfo()
            }
        }
    }

    private fun createViewModel(): ReportSellerViewModel {
        return ViewModelProvider(
            requireActivity(),
            ReportSellerViewModelFactory()
        )[ReportSellerViewModel::class.java]
    }

    private fun fillSpinnerOnSellersChange(binding: FragmentReportSellerBinding) {
        binding.viewModel?.sellers?.observe(viewLifecycleOwner) { sellers ->
            if (sellers.isEmpty()) return@observe

            val sellersNames = sellers.map { seller -> seller.name ?: "" }
            val itemList = listOf(SELLER_SPINNER_HINT) + sellersNames

            val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, itemList) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val textView = super.getDropDownView(position, convertView, parent) as TextView
                    val textColor = if(position == 0) Color.GRAY else Color.BLACK

                    textView.setTextColor(textColor)

                    return textView
                }

                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val textView = super.getView(position, convertView, parent) as TextView
                    val textColor = if(position == 0) Color.GRAY else Color.BLACK

                    textView.setTextColor(textColor)

                    return textView
                }
            }

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSellers.adapter = adapter
        }
    }

    private fun onlyEnableFilterButtonAfterAllFilledUp(binding: FragmentReportSellerBinding) {
        val selectedSellerSource = binding.viewModel!!.selectedSeller
        mediatorLiveData.removeSource(selectedSellerSource)
        mediatorLiveData.addSource(selectedSellerSource) { value ->
            mediatorLiveData.value = Triple(value, mediatorLiveData.value?.second, mediatorLiveData.value?.third)
        }

        val startDateSource = binding.viewModel!!.startDate
        mediatorLiveData.removeSource(startDateSource)
        mediatorLiveData.addSource(startDateSource) { value ->
            mediatorLiveData.value = Triple(mediatorLiveData.value?.first, value, mediatorLiveData.value?.third)
        }

        val endDateSource = binding.viewModel!!.endDate
        mediatorLiveData.removeSource(endDateSource)
        mediatorLiveData.addSource(endDateSource) { value ->
            mediatorLiveData.value = Triple(mediatorLiveData.value?.first, mediatorLiveData.value?.second, value)
        }

        mediatorLiveData.observe(viewLifecycleOwner) { pair ->
            val (seller, startDate, endDate) = pair
            val isEnabled = seller != null && startDate != null && endDate != null
            binding.filterSellerSales.isEnabled = isEnabled
        }
    }

    private fun datePickersObservers(binding: FragmentReportSellerBinding) {
        binding.viewModel!!.openStartDatePicker.observe(viewLifecycleOwner){
            if(it) {
                DatePickerBuilder.buildDatePicker({ millis ->
                    binding.viewModel!!.onSelectStartDate(millis)
                }).show(parentFragmentManager, parentFragment.toString())
            }
        }

        binding.viewModel!!.openEndDatePicker.observe(viewLifecycleOwner){
            if(it) {
                DatePickerBuilder.buildDatePicker({ millis ->
                    binding.viewModel!!.onSelectEndDate(millis)
                }).show(parentFragmentManager, parentFragment.toString())
            }
        }
    }

    private fun setSpinnerOnItemSelectedListener(binding: FragmentReportSellerBinding){
        binding.spinnerSellers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.viewModel!!.onSelectSeller(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}