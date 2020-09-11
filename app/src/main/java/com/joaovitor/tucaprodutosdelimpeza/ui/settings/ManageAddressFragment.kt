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
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogManageAddressBinding
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

        binding.addStreet.setOnClickListener {
            createDialogAddAddress(AddressType.STREET)
        }

        binding.addNeighborhood.setOnClickListener {
            createDialogAddAddress(AddressType.NEIGHBORHOOD)
        }

        binding.addCity.setOnClickListener {
            createDialogAddAddress(AddressType.CITY)
        }

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

        binding.deleteStreet.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->

                    createDialogDeleteAddress(AddressType.STREET, items[index])
                })
        }

        binding.deleteNeighborhood.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->

                    createDialogDeleteAddress(AddressType.NEIGHBORHOOD, items[index])
                })
        }

        binding.deleteCity.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->

                    createDialogDeleteAddress(AddressType.CITY, items[index])
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
                .setPositiveButton("Salvar", null)
                .show()}
    }

    private fun createDialogEditAddress(type: AddressType, name: String) {
        val binding: DialogManageAddressBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_manage_address,
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

    private fun createDialogDeleteAddress(type: AddressType, name: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Excluir $name")
                .setMessage("Esta ação irá deletar o(a) "+type.value+" permanentemente")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salvar", null)
                .show()}
    }
}