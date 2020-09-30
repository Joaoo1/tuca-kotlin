package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleInfoBinding


class SaleInfoFragment : Fragment() {

    private lateinit var sale: Sale
    private lateinit var viewModel: SaleInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        sale = arguments?.let { SaleInfoFragmentArgs.fromBundle(it).sale }!!
        activity?.title = String.format(
            resources.getString(R.string.title_fragment_sale_info),
            sale.saleId)

        // Inflate the layout for this fragment
        val binding: FragmentSaleInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_info, container, false)

        val adapter = SaleInfoListAdapter()
        binding.productsList.adapter = adapter

        //Create the viewModel
        val viewModelFactory = SaleInfoViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)
            .get(SaleInfoViewModel::class.java)
        
        viewModel.setSale(sale)

        viewModel.sale.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.listData = it.products
            }
        })

        viewModel.navigateToEditProducts.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController()
                    .navigate(SaleInfoFragmentDirections.actionSalesInfoFragmentToSaleEditProductsFragment(sale))
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateBack.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().popBackStack()
                viewModel.doneNavigating()
            }
        })

        viewModel.openPaymentDialog.observe(viewLifecycleOwner, Observer {
            if(it) {
                createPaymentDialog()
                viewModel.doneNavigating()
            }
        })

        viewModel.openDeleteDialog.observe(viewLifecycleOwner, Observer {
            if(it) {
                createDeleteDialog()
                viewModel.doneNavigating()
            }
        })

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_register_payment -> viewModel.onClickRegisterPayment()
            R.id.action_delete_sale -> viewModel.onClickDeleteSale()
            R.id.action_edit_products -> viewModel.onClickEditProducts()
            R.id.action_print -> { /* TODO: Implement print logic */ }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createPaymentDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_payment, null)
        val textInput: TextInputLayout = view.findViewById(R.id.paid_value)
        textInput.helperText = String.format(resources
            .getString(R.string.dialog_payment_paid_value_helper_text), sale.toReceive)

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(view)
                .setTitle(getString(R.string.dialog_payment_title))
                .setNegativeButton(getString(R.string.dialog_payment_negative_button))
                    { _, _ -> viewModel.doneNavigating() }
                .setPositiveButton(getString(R.string.dialog_payment_positive_button))
                    { _, _ -> viewModel.registerPayment(textInput.editText!!.text.toString()) }
                .show()
        }
    }

    private fun createDeleteDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.dialog_delete_sale_title))
                .setMessage(getString(R.string.dialog_delete_sale_message))
                .setNegativeButton(getString(R.string.dialog_delete_sale_negative_button), null)
                .setPositiveButton(getString(R.string.dialog_delete_sale_positive_button)) { _, _ -> viewModel.deleteSale() }
                .show()
        }
    }
}