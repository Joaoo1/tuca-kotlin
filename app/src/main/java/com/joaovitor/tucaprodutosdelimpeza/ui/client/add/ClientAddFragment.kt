package com.joaovitor.tucaprodutosdelimpeza.ui.client.add

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientAddBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong
import kotlinx.coroutines.launch

class ClientAddFragment : Fragment() {

    private lateinit var viewModel: ClientAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentClientAddBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_client_add, container, false)

        //Create the viewModel
        val viewModelFactory = ClientAddViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ClientAddViewModel::class.java]

        viewModel.streets.observe(viewLifecycleOwner) {
            it?.let {
                val autoCompleteAdapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_list_item_1, it.toTypedArray())

                (binding.street.editText as MaterialAutoCompleteTextView).setAdapter(autoCompleteAdapter)
            }
        }

        viewModel.navigateToManageAddress.observe(viewLifecycleOwner) {
            if(it){
                findNavController().navigate(
                    ClientAddFragmentDirections.actionClientAddFragmentToManageAddressFragment())
                viewModel.doneNavigating()
            }
        }

       viewModel.openSelectNeighborhood.observe(viewLifecycleOwner) {
           if (it) {
            createDialogSelectAddress(viewModel.neighborhoods) { _, index ->
                viewModel.onNeighborhoodSelected(viewModel.neighborhoods[index])
            }
               viewModel.doneNavigating()
           }
       }

        viewModel.openSelectCity.observe(viewLifecycleOwner) {
           if (it) {
            createDialogSelectAddress(viewModel.cities)
            { _, index -> viewModel.onCitySelected(viewModel.cities[index]) }

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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.client_add, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_address -> {
                        viewModel.onClickAddAddress()
                        true
                    }
                    R.id.action_save -> {
                        viewLifecycleOwner.lifecycleScope.launch { viewModel.onClickSave() }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        /**
         * In the case that a address is added
         * it ensures that the address fields are up to date
         * when user backs to screen
         */
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchAddress()
        }
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