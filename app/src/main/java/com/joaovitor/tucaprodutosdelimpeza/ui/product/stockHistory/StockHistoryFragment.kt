package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductListBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentStockHistoryBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.product.list.ProductListAdapter

class StockHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val productId = StockHistoryFragmentArgs.fromBundle(requireArguments()).productId

        // Inflate the layout for this fragment
        val binding: FragmentStockHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stock_history, container, false)

        // Create the viewModel
        val viewModelFactory = StockHistoryViewModelFactory(productId)
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(StockHistoryViewModel::class.java)

        //Setting up the recycler view
        val adapter = StockHistoryListAdapter()
        binding.stockHistoriesList.adapter = adapter
        viewModel.stockHistories.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.listData = it
            }
        })

        return binding.root
    }

}