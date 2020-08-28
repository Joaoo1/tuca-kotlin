package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

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
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleAddBinding
// import com.joaovitor.tucaprodutosdelimpeza.ui.sale.list.SalesListFragmentDirections

class SaleAddFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding: FragmentSaleAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_add, container,false)

        val viewModelFactory = SaleAddViewModelFactory()
        val viewModel: SaleAddViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SaleAddViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.navigateToSelectClient.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController()
                    .navigate(SaleAddFragmentDirections.actionSalesAddFragmentToSelectClientFragment())
                viewModel.doneNavigation()
            }
        })
        return binding.root
    }

}