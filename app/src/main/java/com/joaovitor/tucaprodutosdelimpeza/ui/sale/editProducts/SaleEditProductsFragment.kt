package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogAddProductBinding

class SaleEditProductsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_fragment_sale_edit)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sale_edit_products, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_product -> createAddProductDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createAddProductDialog() {
        val binding: DialogAddProductBinding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_add_product, null,false)
        context?.let {
            val dialog = MaterialAlertDialogBuilder(it)
                .setView(binding.root)
                .show()

            binding.close.setOnClickListener {
                dialog.dismiss()
            }
        }


    }
}