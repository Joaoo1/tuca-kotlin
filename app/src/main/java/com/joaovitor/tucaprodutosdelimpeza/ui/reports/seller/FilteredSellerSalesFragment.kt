package com.joaovitor.tucaprodutosdelimpeza.ui.reports.seller

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentFilteredSalesBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales.FilteredSalesFragmentDirections
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.list.SaleListAdapter
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class FilteredSellerSalesFragment : Fragment() {

    private lateinit var viewModel: ReportSellerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFilteredSalesBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_filtered_sales,container, false)

        viewModel = createViewModel()

        //Setting up RecyclerView
        val listAdapter = FilteredSellerSalesListAdapter(SaleListAdapter.SaleListener { sale ->
            viewModel.onSaleClicked(sale)
        })
        binding.filteredSales.adapter = listAdapter
        viewModel.filteredSales.observe(viewLifecycleOwner){
            it?.let {
                listAdapter.submitList(it)
            }
        }

        //Navigate to Info Sale Fragment listener
        viewModel.navigateToInfoSale.observe(viewLifecycleOwner){ sale ->
            sale?.let {
                findNavController()
                    .navigate(
                        FilteredSalesFragmentDirections.actionFilteredSalesFragmentToSalesInfoFragment(it)
                    )
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

    private fun createViewModel(): ReportSellerViewModel {
        return ViewModelProvider(
            requireActivity(),
            ReportSellerViewModelFactory()
        )[ReportSellerViewModel::class.java]
    }
}