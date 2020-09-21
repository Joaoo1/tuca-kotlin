package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleListBinding


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
        viewModel.sales.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.saleList = it
            }
        })

        //Floating button click
        binding.fab.setOnClickListener { viewModel.onClickFab() }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController()
                    .navigate(SaleListFragmentDirections.actionSalesListFragmentToSalesAddFragment())
                viewModel.doneNavigation()
            }
        })

        //Navigate to Edit Fragment listener
        viewModel.navigateToInfo.observe(viewLifecycleOwner, Observer { sale ->
            sale?.let {
                findNavController()
                    .navigate(
                        SaleListFragmentDirections.actionSalesListFragmentToSalesInfoFragment(
                            sale
                        )
                    )
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_list, menu)
        searchOnList(menu.findItem(R.id.action_search_sales))
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_filter_sales -> createFiltersDialog()
            R.id.action_refresh_sales -> viewModel.refreshSalesList()
        }
        return super.onOptionsItemSelected(item)
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

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(1)?.isChecked = true
    }

    private fun createFiltersDialog() {
        FilterSalesDialog(this).show()
    }

}