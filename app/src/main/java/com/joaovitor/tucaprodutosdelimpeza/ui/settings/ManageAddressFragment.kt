package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.data.model.AddressType
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogManageAddressBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentManageAddressBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class ManageAddressFragment : Fragment() {

    private lateinit var viewModel: ManageAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment,
        val binding: FragmentManageAddressBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_manage_address, container, false)

        //Create the viewModel
        val viewModelFactory = ManageAddressViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ManageAddressViewModel::class.java)

        viewModel.openDialogAddAddress.observe(viewLifecycleOwner) {
            it?.let{
                createDialogAddAddress(it)
                viewModel.dialogDoneOpening()
            }
        }

        viewModel.openDialogEditAddress.observe(viewLifecycleOwner) {
            it?.let{
                createDialogSelectAddress(
                    it.addressList.map { address -> address.name }.toTypedArray()
                ) { _, i ->
                    val selectedAddress = it.addressList[i]
                    selectedAddress.type = it.addressType
                    createDialogEditAddress(selectedAddress)
                }
                viewModel.dialogDoneOpening()
            }
        }

        viewModel.openDialogDeleteAddress.observe(viewLifecycleOwner) {
            it?.let{
                createDialogSelectAddress(
                    it.addressList.map { address -> address.name }.toTypedArray()
                ) { _, i ->
                    val selectedAddress = it.addressList[i]
                    selectedAddress.type = it.addressType
                    createDialogDeleteAddress(selectedAddress)
                }
                viewModel.dialogDoneOpening()
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    private fun createDialogAddAddress(type: AddressType) {
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

    private fun createDialogEditAddress(address: Address) {
        val binding: DialogManageAddressBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_manage_address,
            null,
            false)

        binding.name = address.name

        context?.let {

            MaterialAlertDialogBuilder(it)
                .setTitle("Editar "+address.type?.value)
                .setMessage("Digite o novo nome do(a) "+address.type?.value)
                .setView(binding.root)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar") { _, _ ->
                    val newName= binding.addressName.editText!!.text.toString()
                    viewModel.onClickEditAddressPositiveButton(address, newName)}
                .show()}
    }

    private fun createDialogDeleteAddress(address: Address) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Excluir "+address.name)
                .setMessage("Esta ação irá deletar o(a) "+address.type?.value+" permanentemente")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar") { _, _ ->
                    viewModel.onClickDeleteAddressPositiveButton(address)}
                .show()}
    }
}