package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditStockBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong
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