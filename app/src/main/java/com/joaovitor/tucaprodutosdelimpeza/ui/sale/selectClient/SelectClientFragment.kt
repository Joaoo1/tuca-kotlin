package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSelectClientBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.add.SaleAddViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.add.SaleAddViewModelFactory

class SelectClientFragment : Fragment() {

    private lateinit var listAdapter: SelectClientListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val binding: FragmentSelectClientBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_select_client, container, false)

        //Create the Add viewModel
        val viewModelFactory = SaleAddViewModelFactory()
        val viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(SaleAddViewModel::class.java)

        //Setting up Recycler View
        listAdapter = SelectClientListAdapter(SelectClientListAdapter.SelectClientListener { client ->
            viewModel.onClientClicked(client)
        })
        binding.clientsList.adapter = listAdapter
        viewModel.allClients.observe(viewLifecycleOwner, Observer {
            it?.let { listAdapter.allClients = it }
        })

        viewModel.navigateBackToAdd.observe(viewLifecycleOwner, Observer {
            if(it) {
                this.findNavController()
                    .popBackStack()
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_list, menu)
        //Implement SearchView
        searchOnList(menu.findItem(R.id.action_search))
        super.onCreateOptionsMenu(menu, inflater)
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