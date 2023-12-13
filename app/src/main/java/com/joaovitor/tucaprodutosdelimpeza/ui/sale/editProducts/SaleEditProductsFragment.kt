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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogAddProductBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleEditProductsBinding
import com.joaovitor.tucaprodutosdelimpeza.util.SwipeToDeleteCallback
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class SaleEditProductsFragment : Fragment() {

    private lateinit var sale: Sale
    private lateinit var viewModel: SaleEditProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        sale = SaleEditProductsFragmentArgs.fromBundle(requireArguments()).sale

        /* Create the viewModel */
        val viewModelFactory = SaleEditProductsViewModelFactory(sale)
        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(SaleEditProductsViewModel::class.java)

        /* Inflate the layout for this fragment */
        val binding: FragmentSaleEditProductsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_edit_products, container, false)

        /* Setting up the recycler view */
        val adapter = SaleEditProductsListAdapter()
        binding.productsList.adapter = adapter
        viewModel.products.observe(viewLifecycleOwner) {
            it?.let { adapter.listData = it }
        }
        //Swipe to delete
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removeProductAt(viewHolder.absoluteAdapterPosition)
                adapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.productsList)

        viewModel.openAddProductDialog.observe(viewLifecycleOwner) {
            if(it) {
                createAddProductDialog()
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) {
            if(it) this.findNavController().popBackStack()
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_product -> viewModel.onClickAddProduct()
            R.id.action_save -> viewModel.onClickSave()
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

            /* Setting up products AutoCompleteTextView */
            viewModel.allProducts.observe(viewLifecycleOwner) {
                it?.let {
                    /**
                     * Set a list with only the products name
                     * And convert it to array
                     * So it can be used by the adapter
                     */
                    val arrayProductsName = it.map { product -> product.name }.toTypedArray()

                    val autoCompleteAdapter = ArrayAdapter(requireContext(),
                        android.R.layout.simple_list_item_1, arrayProductsName)

                    (binding.product.editText as MaterialAutoCompleteTextView)
                        .setAdapter(autoCompleteAdapter)
                }
            }

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