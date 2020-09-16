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
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditCadasterBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditStockBinding
//import com.joaovitor.tucaprodutosdelimpeza.ui.product.list.ProductListFragmentDirections

class ProductEditStockFragment : Fragment() {

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
            if (isChecked) {
                binding.stock.setTextColor(resources.getColor(R.color.colorGreenStock))
            } else {
                binding.stock.setTextColor(resources.getColor(R.color.colorLightGray))
            }
        }

        // Create the viewModel
        val viewModelFactory = ProductEditViewModelFactory(null)
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ProductEditViewModel::class.java)

        viewModel.product.observe(viewLifecycleOwner, Observer {
            binding.product = it
        })



        val stockHistoryButton = binding.stockHistory
        stockHistoryButton.setOnClickListener {
            this.findNavController()
                .navigate(ProductEditFragmentDirections.actionProductEditFragmentToStockHistoryFragment())
        }
        return binding.root
    }
}