package com.joaovitor.tucaprodutosdelimpeza.ui.client.add

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientAddBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.settings.ManageAddressFragment

class ClientAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentClientAddBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_client_add, container, false)

        binding.neighborhood.editText?.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->
                    binding.neighborhood.editText?.setText(items[index])
                })
        }

        binding.city.editText?.setOnClickListener {
            val items = arrayOf("Teste", "Teste1", "teste2")
            createDialogSelectAddress(
                items,
                DialogInterface.OnClickListener {
                        _, index ->
                    binding.city.editText?.setText(items[index])
                })
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
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