package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockMovements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentStockMovementsBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class StockMovementsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val productId = StockMovementsFragmentArgs.fromBundle(requireArguments()).productId

        // Inflate the layout for this fragment
        val binding: FragmentStockMovementsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stock_movements, container, false)

        // Create the viewModel
        val viewModelFactory = StockMovementsViewModelFactory(productId)
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(StockMovementsViewModel::class.java)

        //Setting up the recycler view
        val adapter = StockMovementsListAdapter()
        binding.stockHistoriesList.adapter = adapter
        viewModel.stockHistories.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitStockMovementsList(it)
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

}