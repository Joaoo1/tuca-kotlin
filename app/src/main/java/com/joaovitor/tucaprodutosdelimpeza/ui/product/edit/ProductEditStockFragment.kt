package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditStockBinding

class ProductEditStockFragment(val product: Product) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding: FragmentProductEditStockBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_edit_stock,
            container,
            false
        )

        binding.switchStock.setOnCheckedChangeListener { _, isChecked ->
            binding.stock.isEnabled = isChecked
            context?.let {
                if (isChecked) {
                    binding.stock.setTextColor(ContextCompat.getColor(it, R.color.colorGreenStock))
                } else {
                    binding.stock.setTextColor(ContextCompat.getColor(it, R.color.colorLightGray))
                }
            }
        }

        // Create the viewModel
        val viewModelFactory = ProductEditViewModelFactory(product)
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ProductEditViewModel::class.java)

        viewModel.product.observe(viewLifecycleOwner, Observer {
            println("teste")
        })

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }
}