package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogEditAddressBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentManageAddressBinding

class ManageAddressFragment : Fragment() {

    enum class AddressType(val value: String) {
        STREET("rua"),
        NEIGHBORHOOD("bairro"),
        CITY("cidade"),
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment,
        val binding: FragmentManageAddressBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_manage_address, container, false)

        binding.editStreet.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->

                    createDialogEditAddress(AddressType.STREET, items[index])
                })
        }

        binding.editNeighborhood.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->

                    createDialogEditAddress(AddressType.NEIGHBORHOOD, items[index])
                })
        }

        binding.editCity.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->

                    createDialogEditAddress(AddressType.CITY, items[index])
                })
        }

        return binding.root
    }

    private fun createDialogSelectAddress(
        items: Array<String>,
        clickListener: DialogInterface.OnClickListener?
    ) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Selecione um item para editar")
                .setItems(items, clickListener)
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun createDialogEditAddress(type: AddressType, name: String) {
        val binding: DialogEditAddressBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_edit_address,
            null,
            false)

        binding.name = name

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Editar "+type.value)
                .setMessage("Digite o novo nome do(a) "+type.value)
                .setView(binding.root)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar", null)
                .show()}
    }
}