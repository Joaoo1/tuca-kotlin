package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductListBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class ProductListFragment : Fragment() {

    private lateinit var listAdapter: ProductListAdapter
    private lateinit var viewModel: ProductListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentProductListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_list, container, false)

        // Create the viewModel
        val viewModelFactory = ProductListViewModelFactory()
        viewModel = ViewModelProvider(this,viewModelFactory)[ProductListViewModel::class.java]

        //Setting up the recycler view
        listAdapter = ProductListAdapter(ProductListAdapter.ProductListener { product ->
            viewModel.onProductClicked(product)
        })
        listAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.productsList.adapter = listAdapter
        viewModel.products.observe(viewLifecycleOwner) {
            it?.let {
                listAdapter.productsList = it
            }
        }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner) { navigate ->
            if(navigate) {
                this.findNavController()
                    .navigate(ProductListFragmentDirections.actionProductListFragmentToProductAddFragment())
                viewModel.doneNavigating()
            }
        }

        //Navigate to Edit Fragment listener
        viewModel.navigateToEdit.observe(viewLifecycleOwner) { product ->
            product?.let {
                this.findNavController()
                    .navigate(ProductListFragmentDirections.actionProductListFragmentToProductEditFragment(it))
                viewModel.doneNavigating()
            }
        }

        //Floating button click
        binding.fab.setOnClickListener { viewModel.onClickFab() }

        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                context?.toastLong(it)
                viewModel.doneShowError()
            }
        }

        viewModel.info.observe(viewLifecycleOwner) {
            it?.let {
                context?.toast(it)
                viewModel.doneShowInfo()
            }
        }

        viewModel.showProgressBar.observe(viewLifecycleOwner) {
            if(it) {
                (activity as MainActivity).showProgressBar()
            } else {
                (activity as MainActivity).hideProgressBar()
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_list, menu)
                searchOnList(menu.findItem(R.id.action_search))
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_refresh_list -> {
                        viewModel.onClickRefreshList()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(3)?.isChecked = true
    }

    private fun searchOnList(search: MenuItem) {
        val searchView = search.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                listAdapter.filter.filter(newText)
                listAdapter.notifyDataSetChanged()
                return false
            }
        })
    }
}