package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientListBinding

class ClientListFragment : Fragment() {

    private lateinit var viewModel: ClientListViewModel
    private lateinit var listAdapter: ClientListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        //Inflate the layout for this fragment
        val binding: FragmentClientListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_client_list, container, false
        )

        //Create the View Model
        val viewModelFactory = ClientListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClientListViewModel::class.java)

        //Setting up the recycler view
        listAdapter = ClientListAdapter(ClientListAdapter.ClientListener { client ->
            viewModel.onClientClicked(client) })
        binding.clientsList.adapter = listAdapter
        viewModel.clients.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitClientList(it)
            }
        })

        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController()
                    .navigate(ClientListFragmentDirections.actionClientListFragmentToClientAddFragment())
                viewModel.doneNavigation()
            }
        })

        viewModel.navigateToInfo.observe(viewLifecycleOwner, Observer { client ->
            client?.let {
                findNavController()
                .navigate(ClientListFragmentDirections.actionClientListFragmentToClientInfoFragment(it))
                viewModel.doneNavigation()
            }
        })

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_list, menu)
        //Implement SearchView
        searchOnList(menu.findItem(R.id.action_search))
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(2)?.isChecked = true
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