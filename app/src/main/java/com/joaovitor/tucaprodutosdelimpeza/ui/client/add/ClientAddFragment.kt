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
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientAddBinding

class ClientAddFragment : Fragment() {

    private lateinit var viewModel: ClientAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)


        // Inflate the layout for this fragment
        val binding: FragmentClientAddBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_client_add, container, false)

        //Create the viewModel
        val viewModelFactory = ClientAddViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ClientAddViewModel::class.java)

        viewModel.streets.observe(viewLifecycleOwner, Observer {
            it?.let {
                val autoCompleteAdapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_list_item_1, it.toTypedArray())

                (binding.street.editText as MaterialAutoCompleteTextView).setAdapter(autoCompleteAdapter)
            }
        })

        viewModel.navigateToManageAddress.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(
                    ClientAddFragmentDirections.actionClientAddFragmentToManageAddressFragment())
                viewModel.doneNavigating()
            }
        })

       viewModel.openSelectNeighborhood.observe(viewLifecycleOwner, Observer {
           if (it) {
            createDialogSelectAddress(
                viewModel.neighborhoods,
                DialogInterface.OnClickListener {
                        _, index ->
                    viewModel.onNeighborhoodSelected(viewModel.neighborhoods[index]) })
            viewModel.doneNavigating()
           }
       })

        viewModel.openSelectCity.observe(viewLifecycleOwner, Observer {
           if (it) {
            createDialogSelectAddress(
                viewModel.cities,
                DialogInterface.OnClickListener {
                        _, index ->
                    viewModel.onCitySelected(viewModel.cities[index]) })
            viewModel.doneNavigating()
           }
       })

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_address -> viewModel.onClickAddAddress()
            R.id.action_save -> viewModel.onClickSave()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        /**
         * In the case that a address is added
         * it ensures that the address fields are up to date
         * when user backs to screen
         */
        viewModel.fetchAddress()
    }

    private fun createDialogSelectAddress(
        items: Array<String>,
        clickListener: DialogInterface.OnClickListener?
    ) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.dialog_select_address_title))
                .setItems(items, clickListener)
                .setNegativeButton(getString(R.string.dialog_select_address_negative_button), null)
                .show()
        }
    }

}