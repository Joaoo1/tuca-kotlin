package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleInfoBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleListBinding


class SaleInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSaleInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_info, container, false)
        return binding.root
    }
}