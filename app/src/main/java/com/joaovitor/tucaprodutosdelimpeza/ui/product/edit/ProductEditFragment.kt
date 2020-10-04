package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class ProductEditFragment : Fragment() {

    private lateinit var product: Product
    private lateinit var viewModel: ProductEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        product = arguments?.let { ProductEditFragmentArgs.fromBundle(it).product }!!

        // Inflate the layout for this fragment
        val binding = FragmentProductEditBinding.inflate(inflater,container,false)

        // Setting up viewPager with tabLayout
        val sectionsPagerAdapter = context?.let {
            SectionsPagerAdapter(it,childFragmentManager, product)
        }
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs = binding.tabLayout
        tabs.setupWithViewPager(viewPager)
        tabs.bringToFront() // Tabs don't switch on click without it

        // Create the viewModel
        val viewModelFactory = ProductEditViewModelFactory(product)
        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ProductEditViewModel::class.java)

        viewModel.navigateBack.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
                viewModel.doneNavigating()
            }
        }

        viewModel.openDialogDelete.observe(viewLifecycleOwner) {
            if(it) {
                createDeleteProductDialog()
                viewModel.doneNavigating()
            }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete_product -> viewModel.onClickDeleteProduct()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDeleteProductDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.dialog_delete_product_title))
                .setMessage(getString(R.string.dialog_delete_product_message))
                .setNegativeButton(getString(R.string.dialog_delete_product_negative_button), null)
                .setPositiveButton(getString(R.string.dialog_delete_product_positive_button))
                {_, _ -> viewModel.onClickDeleteProductPositive()}
                .show()
        }
    }

}