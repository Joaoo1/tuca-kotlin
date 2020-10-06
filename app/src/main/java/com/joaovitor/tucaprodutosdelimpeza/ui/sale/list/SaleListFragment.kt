package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleListBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class SaleListFragment : Fragment() {

    private lateinit var viewModel: SaleListViewModel
    private lateinit var listAdapter: SaleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentSaleListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_list, container, false
        )

        //Create the viewModel
        val viewModelFactory = SaleListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SaleListViewModel::class.java)

        //Setting up the recycler view
        listAdapter = SaleListAdapter(SaleListAdapter.SaleListener { sale ->
            viewModel.onSaleClicked(sale)
        })
        binding.salesList.adapter = listAdapter
        viewModel.sales.observe(viewLifecycleOwner) {
            it?.let {
                listAdapter.saleList = it
            }
        }
        viewModel.filteredSales.observe(viewLifecycleOwner) {
            it?.let {
                listAdapter.saleList = it
            }
        }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController()
                    .navigate(SaleListFragmentDirections.actionSaleListFragmentToSalesAddFragment())
                viewModel.doneNavigating()
            }
        }

        //Navigate to Edit Fragment listener
        viewModel.navigateToInfo.observe(viewLifecycleOwner) { sale ->
            sale?.let {
                findNavController()
                    .navigate(
                        SaleListFragmentDirections.actionSalesListFragmentToSalesInfoFragment(sale)
                    )
                viewModel.doneNavigating()
            }
        }

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

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_filter_sales -> createFiltersDialog()
            R.id.action_search_sales -> searchOnList(item)
            R.id.action_refresh_sales -> viewModel.onClickRefreshList()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchOnList(search: MenuItem) {
        val searchView = search.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.filterSales(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()) {
                    viewModel.filterSales(newText)
                }
                return false
            }

        })

    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(1)?.isChecked = true
    }

    private fun createFiltersDialog() {
        FilterSalesDialog(this).show()
    }
}