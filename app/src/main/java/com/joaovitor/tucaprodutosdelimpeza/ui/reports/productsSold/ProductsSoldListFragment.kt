package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductsSoldListBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class ProductsSoldListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentProductsSoldListBinding =  DataBindingUtil.inflate(
            inflater,R.layout.fragment_products_sold_list, container, false)

        //Create the viewModel
        val viewModelFactory = ReportProductsSoldViewModelFactory()
        val viewModel = ViewModelProvider(requireActivity(),viewModelFactory)
            .get(ReportProductsSoldViewModel::class.java)

        //Setting up RecyclerView
        val listAdapter = ProductsSoldListAdapter()
        binding.productsSoldList.adapter = listAdapter
        viewModel.productsSoldList.observe(viewLifecycleOwner) {
            it?.let {
                listAdapter.submitList(it)
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

        return binding.root
    }
}