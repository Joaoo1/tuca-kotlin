package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleAddBinding
import com.joaovitor.tucaprodutosdelimpeza.util.SaleProductItemDecoration
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong
import java.util.Calendar

class SaleAddFragment : Fragment() {

    companion object {
        const val REQUEST_ENABLED_BT = 1
    }

    private lateinit var viewModel: SaleAddViewModel
    private lateinit var listAdapter: SaleProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentSaleAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_add, container, false
        )

        //Create the viewModel
        val viewModelFactory = SaleAddViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(SaleAddViewModel::class.java)

        // Setting up the RecyclerView
        listAdapter = SaleProductsAdapter(SaleProductsAdapter.DeleteProductSaleListener {
            viewModel.onClickDeleteProduct(it)
            listAdapter.notifyItemRemoved(it)
        })
        binding.productsList.addItemDecoration(SaleProductItemDecoration(requireContext()))
        binding.productsList.adapter = listAdapter
        viewModel.products.observe(viewLifecycleOwner) {
            it?.let {
                listAdapter.addHeaderAndSubmitList(it)
            }
        }

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

        viewModel.navigateToSelectClient.observe(viewLifecycleOwner) {
            if (it) {
                findNavController()
                    .navigate(SaleAddFragmentDirections.actionSalesAddFragmentToSelectClientFragment())
                viewModel.doneNavigating()
            }
        }

        viewModel.requestBluetoothOn.observe(viewLifecycleOwner) {
            if(it) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLED_BT)
                viewModel.doneNavigating()
            }
        }

        viewModel.dialogBluetoothOff.observe(viewLifecycleOwner) {
            if(it) {
                createBluetoothDialog()
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

        binding.client.setEndIconOnClickListener {
            viewModel.navigateToSelectClient()
        }

        binding.date.setEndIconOnClickListener {
            viewModel.onSaleDateSelect(Calendar.getInstance().timeInMillis)
        }

        binding.addProduct.setOnClickListener {
            viewModel.addProduct(binding.product.editText?.text.toString())
            binding.product.editText?.setText("")
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save_sale -> viewModel.onClickAddSale(requireContext())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createBluetoothDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_activate_bluetooth_title))
                .setMessage(getString(R.string.dialog_activate_bluetooth_message))
                .setNegativeButton(getString(R.string.dialog_activate_bluetooth_negative_button), null)
                .setPositiveButton(getString(R.string.dialog_activate_bluetooth_positive_button))
                {_, _ ->  viewModel.onClickBluetoothDialogPositive() }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLED_BT) viewModel.onBluetoothResult(resultCode, requireContext())
        super.onActivityResult(requestCode, resultCode, data)
    }
}