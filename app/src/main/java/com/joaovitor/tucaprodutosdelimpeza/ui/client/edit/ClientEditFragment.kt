package com.joaovitor.tucaprodutosdelimpeza.ui.client.edit

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientAddBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientEditBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.product.edit.ProductEditFragmentArgs
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.add.SaleAddViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.add.SaleAddViewModelFactory

class ClientEditFragment : Fragment() {

    private lateinit var viewModel: ClientEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        //Create the viewModel
        val viewModelFactory = ClientEditViewModelFactory(ClientEditFragmentArgs.fromBundle(requireArguments()).client)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClientEditViewModel::class.java)

        // Inflate the layout for this fragment
        val binding: FragmentClientEditBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_client_edit, container, false)

        viewModel.streets.observe(viewLifecycleOwner, Observer {
            it?.let {
                val arrayProductsName = it.toTypedArray()
                val autoCompleteAdapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_list_item_1, arrayProductsName)

                (binding.street.editText as MaterialAutoCompleteTextView).setAdapter(autoCompleteAdapter)
            }
        })

        viewModel.openSelectNeighborhood.observe(viewLifecycleOwner, Observer {
            if(it) {
                createDialogSelectAddress(
                    viewModel.neighborhoods,
                    DialogInterface.OnClickListener {
                            _, index ->
                        viewModel.onNeighborhoodSelected(viewModel.neighborhoods[index])
                    })
            }
        })

        viewModel.openSelectCity.observe(viewLifecycleOwner, Observer {
            if(it) {
                createDialogSelectAddress(
                    viewModel.cities,
                    DialogInterface.OnClickListener {
                            _, index ->
                        viewModel.onCitySelected(viewModel.cities[index])
                    })
            }
        })

        viewModel.navigateToManageAddress.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(
                    ClientEditFragmentDirections.actionClientEditFragmentToManageAddressFragment())
                viewModel.doneNavigation()
            }
        })
        viewModel.navigateBack.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigateUp()
                viewModel.doneNavigation()
            }
        })

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_client -> createDeleteClientDialog()
            R.id.action_add_address -> viewModel.onClickMenuItemAddAddress()
            R.id.action_save -> viewModel.onClickSave()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDeleteClientDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.dialog_delete_client_title))
                .setMessage(getString(R.string.dialog_delete_client_message))
                .setNegativeButton(getString(R.string.dialog_delete_client_negative_button),
                    null)
                .setPositiveButton(getString(R.string.dialog_delete_client_positive_button)) { _, _ -> viewModel.onClickDeleteClient() }
                .show()
        }
    }

    private fun createDialogSelectAddress(
        items: Array<String>?,
        clickListener: DialogInterface.OnClickListener?) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Selecione um item")
                .setItems(items, clickListener)
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}