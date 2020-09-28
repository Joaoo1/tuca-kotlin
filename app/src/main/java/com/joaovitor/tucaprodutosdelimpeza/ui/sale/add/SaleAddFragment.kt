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
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleAddBinding
import com.joaovitor.tucaprodutosdelimpeza.util.SaleProductItemDecoration
import java.util.Calendar

class SaleAddFragment : Fragment() {

    private lateinit var viewModel: SaleAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentSaleAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_add, container, false
        )

        //Create the viewModel
        val viewModelFactory = SaleAddViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(SaleAddViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Setting up the RecyclerView
        val adapter = SaleProductsAdapter()
        binding.productsList.addItemDecoration(SaleProductItemDecoration(requireContext()))
        binding.productsList.adapter = adapter
        viewModel.products.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

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

        /* Setting up products AutoCompleteTextView */
        viewModel.allProducts.observe(viewLifecycleOwner, Observer {
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
                .setNegativeButton(getString(R.string.dialog_activate_bluetooth_negative_button), null)
                .setPositiveButton(getString(R.string.dialog_activate_bluetooth_positive_button)){_, _ ->  viewModel.onClickAddSale() }
                .show()
        }
    }
}