package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditBinding

class ProductEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding = FragmentProductEditBinding.inflate(inflater,container,false)

        val sectionsPagerAdapter = context?.let {
            SectionsPagerAdapter(
                it,childFragmentManager,
                Product("a","a",1, 1))
        }
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs = binding.tabLayout
        tabs.setupWithViewPager(viewPager)
        // Tabs don't switch on click without it
        tabs.bringToFront()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete_product -> createDeleteProductDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDeleteProductDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.dialog_delete_product_title))
                .setMessage(getString(R.string.dialog_delete_product_message))
                .setNegativeButton(getString(R.string.dialog_delete_product_negative_button), null)
                .setPositiveButton(getString(R.string.dialog_delete_product_positive_button), null)
                .show()
        }
    }

}