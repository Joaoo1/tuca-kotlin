package com.joaovitor.tucaprodutosdelimpeza.ui.product.add

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductAddBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditStockBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.client.add.ClientAddViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.client.add.ClientAddViewModelFactory

class ProductAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        //Create the viewModel
        val viewModelFactory = ProductAddViewModelFactory()
        val viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ProductAddViewModel::class.java)

        // Inflate the layout for this fragment
        val binding: FragmentProductAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_add, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.manageStock.setOnCheckedChangeListener { _, isChecked ->
            binding.stock.isEnabled = isChecked
            if(!isChecked) {
               // binding.stock.editText?.setText("")
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}