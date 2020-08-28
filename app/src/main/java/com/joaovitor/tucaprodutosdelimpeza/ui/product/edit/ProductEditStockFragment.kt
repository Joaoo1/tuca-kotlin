package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditStockBinding
//import com.joaovitor.tucaprodutosdelimpeza.ui.product.list.ProductListFragmentDirections

class ProductEditStockFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProductEditStockBinding.inflate(inflater,container,false)

        val stockHistory = binding.stockHistory
        stockHistory.setOnClickListener {
            this.findNavController()
                .navigate(ProductEditFragmentDirections.actionProductEditFragmentToStockHistoryFragment())
        }
        return binding.root
    }
}