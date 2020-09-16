package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogAddProductBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleEditProductsBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleInfoBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.info.SaleInfoFragmentArgs
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.info.SaleInfoViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.info.SaleInfoViewModelFactory

class SaleEditProductsFragment : Fragment() {

    private lateinit var sale: Sale

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        sale = arguments?.let { SaleEditProductsFragmentArgs.fromBundle(it).sale }!!

        //Create the viewModel
        val viewModelFactory = SaleInfoViewModelFactory(sale)
        val viewModel = ViewModelProvider(requireActivity(),viewModelFactory)
            .get(SaleInfoViewModel::class.java)

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
        context?.let {
            val dialog = MaterialAlertDialogBuilder(it)
                .setView(binding.root)
                .show()

            binding.close.setOnClickListener {
                dialog.dismiss()
            }
        }


    }
}