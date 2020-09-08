package com.joaovitor.tucaprodutosdelimpeza.ui.client.edit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R

class ClientEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_fragment_client_edit)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_edit, container, false)
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
}