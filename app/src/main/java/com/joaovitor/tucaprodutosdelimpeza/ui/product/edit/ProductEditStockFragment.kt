package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditStockBinding
import kotlinx.android.synthetic.main.app_bar_main.view.*

/**
 * FIXME: Two way data binding not working properly in this fragment
 * UI not updating when LiveData changes
 */

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

        // Create the viewModel
        val viewModelFactory = ProductEditViewModelFactory(product)
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ProductEditViewModel::class.java)

        binding.viewModel = viewModel

        binding.switchStock.setOnCheckedChangeListener { _, isChecked ->
            binding.stock.visibility = if(isChecked) View.VISIBLE else View.GONE
            viewModel.setManageStock(isChecked)
        }

        return binding.root
    }
}