package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogAddProductBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleEditProductsBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.info.SaleInfoViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.info.SaleInfoViewModelFactory

class SaleEditProductsFragment : Fragment() {

    private lateinit var sale: Sale
    private lateinit var viewModel: SaleEditProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        arguments?.let { sale = SaleEditProductsFragmentArgs.fromBundle(it).sale }

        //Create the viewModel
        val viewModelFactory = SaleEditProductsViewModelFactory(sale)
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)
            .get(SaleEditProductsViewModel::class.java)

        // Inflate the layout for this fragment
        val binding: FragmentSaleEditProductsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_edit_products, container, false)

        val adapter = SaleEditProductsListAdapter()
        binding.productsList.adapter = adapter

        viewModel.sale.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.listData = it.products
            }
        })

        binding.sale = sale

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_product -> createAddProductDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createAddProductDialog() {
        val binding: DialogAddProductBinding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_add_product, null,false)
        context?.let { ctx ->
            val dialog = MaterialAlertDialogBuilder(ctx)
                .setView(binding.root)
                .show()

            binding.lifecycleOwner = this
            binding.viewModel = viewModel

            //Setting up products AutoCompleteTextView
            viewModel.allProducts.observe(viewLifecycleOwner, Observer {
                println(it)
                //Convert product list to a list with only the products name
                val productsName = it?.map { product -> product.name }

                //Null checking and convert list to array
                val arrayProductsName = productsName?.toTypedArray() ?: arrayOf()

                val autoCompleteAdapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_list_item_1, arrayProductsName)
                (binding.product.editText as MaterialAutoCompleteTextView).setAdapter(autoCompleteAdapter)
            })

            binding.addProduct.setOnClickListener {
                viewModel.addProduct(binding.product.editText!!.text.toString())
                dialog.dismiss()
                viewModel.allProducts.removeObservers(viewLifecycleOwner)
            }

            binding.close.setOnClickListener {
                dialog.dismiss()
                viewModel.allProducts.removeObservers(viewLifecycleOwner)
            }
        }

    }
}