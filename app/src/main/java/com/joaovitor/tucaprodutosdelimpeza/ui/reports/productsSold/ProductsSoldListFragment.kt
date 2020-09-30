package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductsSoldListBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentReportProductsSoldBinding

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
        viewModel.productsSoldList.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })

        return binding.root
    }
}