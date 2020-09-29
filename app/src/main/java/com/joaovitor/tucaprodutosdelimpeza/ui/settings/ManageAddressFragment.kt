package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogManageAddressBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentManageAddressBinding

class ManageAddressFragment : Fragment() {

    private lateinit var viewModel: ManageAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment,
        val binding: FragmentManageAddressBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_manage_address, container, false)

        //Create the viewModel
        val viewModelFactory = ManageAddressViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ManageAddressViewModel::class.java)


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.openDialogAddAddress.observe(viewLifecycleOwner, Observer {
            it?.let{
                createDialogAddAddress(it)
                viewModel.dialogDoneOpening()
            }
        })

        viewModel.openDialogEditAddress.observe(viewLifecycleOwner, Observer {
            it?.let{
                createDialogSelectAddress(
                    it.addressList.map { address -> address.name }.toTypedArray(),
                    DialogInterface.OnClickListener {
                        _, i ->
                        createDialogEditAddress(it.addressType,it.addressList[i])
                    }
                )
                viewModel.dialogDoneOpening()
            }
        })

        viewModel.openDialogDeleteAddress.observe(viewLifecycleOwner, Observer {
            it?.let{
                createDialogSelectAddress(
                    it.addressList.map { address -> address.name }.toTypedArray(),
                    DialogInterface.OnClickListener {
                        _, i ->
                        createDialogDeleteAddress(it.addressType,it.addressList[i])
                    }
                )
                viewModel.dialogDoneOpening()
            }
        })

        return binding.root
    }

    private fun createDialogAddAddress(type: ManageAddressViewModel.AddressType) {
        val binding: DialogManageAddressBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_manage_address,
            null,
            false)

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Adicionar "+type.value)
                .setMessage("Digite o nome do(a) "+type.value)
                .setView(binding.root)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar") { _,_ ->
                    viewModel.onClickAddAddressPositiveButton(
                        binding.addressName.editText!!.text.toString(),
                        type
                    )
                }
                .show()}
    }

    private fun createDialogSelectAddress(
        items: Array<String>,
        clickListener: DialogInterface.OnClickListener?
    ) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.dialog_select_address_title)
                .setItems(items, clickListener)
                .setNegativeButton(R.string.dialog_select_address_negative_button, null)
                .show()
        }
    }

    private fun createDialogEditAddress(type: ManageAddressViewModel.AddressType, address: Address) {
        val binding: DialogManageAddressBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_manage_address,
            null,
            false)

        binding.name = address.name

        context?.let {

            MaterialAlertDialogBuilder(it)
                .setTitle("Editar "+type.value)
                .setMessage("Digite o novo nome do(a) "+type.value)
                .setView(binding.root)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar") { _, _ ->
                    address.name = binding.addressName.editText!!.text.toString()
                    viewModel.onClickEditAddressPositiveButton(address, type)}
                .show()}
    }

    private fun createDialogDeleteAddress(type: ManageAddressViewModel.AddressType, address: Address) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Excluir "+address.name)
                .setMessage("Esta ação irá deletar o(a) "+type.value+" permanentemente")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar") { _, _ ->
                    viewModel.onClickDeleteAddressPositiveButton(address.id, type)}
                .show()}
    }
}