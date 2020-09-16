package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditCadasterBinding

class ProductEditCadasterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentProductEditCadasterBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_edit_cadaster,
            container,
            false
        )

        // Create the viewModel
        val viewModelFactory = ProductEditViewModelFactory(null)
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ProductEditViewModel::class.java)

        binding.product = viewModel.product.value

        viewModel.product.observe(viewLifecycleOwner, Observer {
            binding.product = it
        })

        return binding.root
    }

}