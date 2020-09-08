package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.joaovitor.tucaprodutosdelimpeza.R

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
}