package com.joaovitor.tucaprodutosdelimpeza.ui.client.add

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientAddBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.client.edit.ClientEditFragmentDirections
import com.joaovitor.tucaprodutosdelimpeza.ui.client.edit.ClientEditViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.client.edit.ClientEditViewModelFactory
import com.joaovitor.tucaprodutosdelimpeza.ui.settings.ManageAddressFragment

class ClientAddFragment : Fragment() {

    private lateinit var viewModel: ClientAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        //Create the viewModel
        val viewModelFactory = ClientAddViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ClientAddViewModel::class.java)

        // Inflate the layout for this fragment
        val binding: FragmentClientAddBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_client_add, container, false)


        viewModel.streets.observe(viewLifecycleOwner, Observer {
            it?.let {
                val arrayProductsName = it.toTypedArray()
                val autoCompleteAdapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_list_item_1, arrayProductsName)

                (binding.street.editText as MaterialAutoCompleteTextView).setAdapter(autoCompleteAdapter)
            }
        })

        binding.viewModel = viewModel

        binding.neighborhood.editText?.setOnClickListener {
            val neighborhoods = viewModel.neighborhoods.toTypedArray()
            createDialogSelectAddress(
                neighborhoods,
                DialogInterface.OnClickListener {
                        _, index ->
                    binding.neighborhood.editText?.setText(neighborhoods[index])
                })
        }

        binding.city.editText?.setOnClickListener {
            val cities = viewModel.cities.toTypedArray()
            createDialogSelectAddress(
                cities,
                DialogInterface.OnClickListener {
                        _, index ->
                    binding.city.editText?.setText(cities[index])
                })
        }

        viewModel.navigateToManageAddress.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(
                    ClientAddFragmentDirections.actionClientAddFragmentToManageAddressFragment())
                viewModel.doneNavigation()

            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_address -> viewModel.onClickmenuItemAddAddress()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDialogSelectAddress(
        items: Array<String>,
        clickListener: DialogInterface.OnClickListener?
    ) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Selecione um item")
                .setItems(items, clickListener)
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

}