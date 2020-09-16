package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductListBinding

/**
 * A fragment representing a list of Items.
 */
class ProductListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentProductListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_list, container, false)

        // Create the viewModel
        val viewModelFactory = ProductListViewModelFactory()
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ProductListViewModel::class.java)

        //Setting up the recycler view
        val adapter = ProductListAdapter(ProductListAdapter.ProductListener { product ->
            viewModel.onProductClicked(product)
        })
        binding.productsList.adapter = adapter
        viewModel.products.observe(viewLifecycleOwner, Observer {
            it?.let {
                (binding.productsList.adapter as ProductListAdapter).listData = it
            }
        })

        //Floating button click
        binding.fab.setOnClickListener { viewModel.onClickFab() }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer { navigate ->
            if(navigate) {
                this.findNavController()
                    .navigate(ProductListFragmentDirections.actionProductListFragmentToProductAddFragment())
                viewModel.doneNavigation()
            }
        })

        //Navigate to Edit Fragment listener
        viewModel.navigateToEdit.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                this.findNavController()
                    .navigate(ProductListFragmentDirections.actionProductListFragmentToProductEditFragment(it))
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(3)?.isChecked = true
    }
}