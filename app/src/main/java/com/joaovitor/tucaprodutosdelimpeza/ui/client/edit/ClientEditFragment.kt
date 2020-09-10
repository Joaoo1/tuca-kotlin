package com.joaovitor.tucaprodutosdelimpeza.ui.client.edit

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientAddBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientEditBinding

class ClientEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentClientEditBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_client_edit, container, false)

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
                    binding.neighborhood.editText?.setText(items[index])
                })
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete_client -> createDeleteClientDialog()
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
                .setPositiveButton(getString(R.string.dialog_delete_client_positive_button),
                    null)
                .show()
        }
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