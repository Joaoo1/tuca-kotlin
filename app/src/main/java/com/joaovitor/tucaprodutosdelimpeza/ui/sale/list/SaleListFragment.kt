package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleListBinding

class SaleListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_fragment_sale_list)
        val binding: FragmentSaleListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_list, container, false)
        val viewModelFactory = SaleListViewModelFactory()
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(SaleListViewModel::class.java)
        //Setting up the recycler view
        val adapter = SaleListAdapter(SaleListAdapter.SaleListener { sale ->
            viewModel.onSaleClicked(sale)
        })
        binding.salesList.adapter = adapter
        viewModel.sales.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.listData = it
            }
        })
        viewModel.setSales()

        //Floating button click
        binding.fab.setOnClickListener { viewModel.onClickFab() }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer { navigate ->
            if(navigate) {
                findNavController()
                    .navigate(SaleListFragmentDirections.actionSalesListFragmentToSalesAddFragment())
                viewModel.doneNavigation()
            }
        })

        //Navigate to Edit Fragment listener
        viewModel.navigateToInfo.observe(viewLifecycleOwner, Observer { sale ->
            sale?.let {
                findNavController()
                    .navigate(SaleListFragmentDirections.actionSalesListFragmentToSalesInfoFragment(sale))
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_filter_sales -> createFiltersDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createFiltersDialog() {
        val filtersDialog = activity?.let { Dialog(it) }!!
        filtersDialog.setContentView(R.layout.dialog_filter_sales)
        filtersDialog.show()
    }

}