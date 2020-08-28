package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleListBinding

class SalesListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSaleListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_list, container, false)
        val viewModelFactory = SaleListViewModelFactory()
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(SaleListViewModel::class.java)
        //Setting up the recycler view
        val adapter = SaleListAdapter(SaleListAdapter.SaleListener { sale ->
            viewModel.onSaleClicked(sale)
        })
        binding.salesList.adapter = adapter
        viewModel.sales.observe(viewLifecycleOwner, Observer {
            it?.let {
                (binding.salesList.adapter as SaleListAdapter).listData = it
            }
        })
        viewModel.setSales()

        //Floating button click
        binding.fab.setOnClickListener { viewModel.onClickFab() }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer { navigate ->
            if(navigate) {
                findNavController()
                    .navigate(SalesListFragmentDirections.actionSalesListFragmentToSalesAddFragment())
                viewModel.doneNavigation()
            }
        })

        //Navigate to Edit Fragment listener
        viewModel.navigateToInfo.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                findNavController()
                    .navigate(SalesListFragmentDirections.actionSalesListFragmentToSalesInfoFragment())
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }

}