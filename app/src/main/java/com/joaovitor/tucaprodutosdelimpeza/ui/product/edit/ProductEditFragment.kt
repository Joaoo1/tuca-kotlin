package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductEditBinding

class ProductEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProductEditBinding.inflate(inflater,container,false)

        val sectionsPagerAdapter = context?.let {
            SectionsPagerAdapter(
                it,childFragmentManager,
                Product("a","a",1, 1))
        }
        val viewPager = binding.viewPager;
        viewPager.adapter = sectionsPagerAdapter

        val tabs = binding.tabLayout
        tabs.setupWithViewPager(viewPager)
        // Tabs don't switch on click without it
        tabs.bringToFront()

        return binding.root
    }

}