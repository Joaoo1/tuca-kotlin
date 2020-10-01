package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentFilteredSalesBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.list.SaleListAdapter
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.list.SaleListFragmentDirections

class FilteredSalesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentFilteredSalesBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_filtered_sales,container, false)

        //Create the viewModel
        val viewModelFactory = ReportSalesViewModelFactory()
        val viewModel = ViewModelProvider(requireActivity(),viewModelFactory)
            .get(ReportSalesViewModel::class.java)

        //Setting up RecyclerView
        val listAdapter = FilteredSalesListAdapter(SaleListAdapter.SaleListener { sale ->
            viewModel.onSaleClicked(sale)
        })
        binding.filteredSales.adapter = listAdapter
        viewModel.filteredSales.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })

        //Navigate to Info Sale Fragment listener
        viewModel.navigateToInfoSale.observe(viewLifecycleOwner, Observer { sale ->
            sale?.let {
                findNavController()
                    .navigate(
                        FilteredSalesFragmentDirections.actionFilteredSalesFragmentToSalesInfoFragment(it)
                    )
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.print_report, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}