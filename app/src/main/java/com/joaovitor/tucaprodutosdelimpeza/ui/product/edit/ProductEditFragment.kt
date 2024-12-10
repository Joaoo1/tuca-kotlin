package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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
    ): View {
        product = arguments?.let { ProductEditFragmentArgs.fromBundle(it).product }!!

        // Inflate the layout for this fragment
        val binding: FragmentProductEditBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_edit, container, false)

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
        viewModel = ViewModelProvider(this,viewModelFactory)[ProductEditViewModel::class.java]

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.product_edit, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete -> {
                        viewModel.onClickDeleteProduct()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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