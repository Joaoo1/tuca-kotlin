package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleAddBinding
import com.joaovitor.tucaprodutosdelimpeza.util.SaleProductItemDecoration
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Date
import java.util.TimeZone

class SaleAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentSaleAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_add, container, false
        )
        binding.lifecycleOwner = this

        //Create the viewModel
        val viewModelFactory = SaleAddViewModelFactory()
        val viewModel: SaleAddViewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(SaleAddViewModel::class.java)

        binding.viewModel = viewModel

        // Setting up the RecyclerView
        val adapter = SaleProductsAdapter()
        val productsList: RecyclerView = binding.productsList
        context?.let {
            SaleProductItemDecoration(it) }?.let {
                productsList.addItemDecoration(it)
            }
        productsList.adapter = adapter

        viewModel.sale.observe(viewLifecycleOwner, Observer {
                println("PASSOU")
            it?.let {
                adapter.addHeaderAndSubmitList(it.products)
            }
        })

        binding.client.setEndIconOnClickListener {
            viewModel.navigateToSelectClient()
        }

        binding.date.setEndIconOnClickListener {
            viewModel.onSaleDateSelect(Calendar.getInstance().timeInMillis)
            /*val millis = Calendar.getInstance().timeInMillis
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Selecione a data da venda")
            builder.setSelection(millis)
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                viewModel.onSaleDateSelect(it)
            }
            picker.show(parentFragmentManager, picker.toString())*/
        }

        binding.radioButtonPartiallyPaid.setOnCheckedChangeListener { _, isChecked ->
            val visibility = if(isChecked) View.VISIBLE else View.GONE
            binding.paidValue.visibility = visibility
        }

        viewModel.allProducts.observe(viewLifecycleOwner, Observer {
            val productsArray = it?.toTypedArray() ?: arrayOf()
            val autoCompleteAdapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, productsArray)
            (binding.product.editText as MaterialAutoCompleteTextView).setAdapter(autoCompleteAdapter)
        })

        viewModel.navigateToSelectClient.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController()
                    .navigate(SaleAddFragmentDirections.actionSalesAddFragmentToSelectClientFragment())
                viewModel.doneNavigation()
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save_sale -> createBluetoothDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createBluetoothDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_activate_bluetooth_title))
                .setMessage(getString(R.string.dialog_activate_bluetooth_message))
                .setNeutralButton(getString(R.string.dialog_activate_bluetooth_cancel_button), null)
                .setNegativeButton(
                    getString(R.string.dialog_activate_bluetooth_negative_button),
                    null
                )
                .setPositiveButton(
                    getString(R.string.dialog_activate_bluetooth_positive_button),
                    null
                )
                .show()
        }
    }
}