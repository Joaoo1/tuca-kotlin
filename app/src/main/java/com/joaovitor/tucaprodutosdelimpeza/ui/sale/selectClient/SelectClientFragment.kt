package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSelectClientBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.add.SaleAddViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.add.SaleAddViewModelFactory
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class SelectClientFragment : Fragment() {

    private lateinit var listAdapter: SelectClientListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentSelectClientBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_select_client, container, false)

        //Create the Add viewModel
        val viewModelFactory = SaleAddViewModelFactory(requireActivity().application)
        val viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[SaleAddViewModel::class.java]

        //Setting up Recycler View
        listAdapter = SelectClientListAdapter(SelectClientListAdapter.SelectClientListener { client ->
            viewModel.onClientClicked(client)
        })
        binding.clientsList.adapter = listAdapter
        viewModel.allClients.observe(viewLifecycleOwner) {
            it?.let { listAdapter.allClients = it }
        }

        viewModel.navigateBackToAdd.observe(viewLifecycleOwner) {
            if(it) {
                this.findNavController()
                    .popBackStack()
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
                return false
            }
        })
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