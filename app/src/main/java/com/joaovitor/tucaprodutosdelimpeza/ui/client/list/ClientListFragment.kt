package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientListBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class ClientListFragment : Fragment() {

    private lateinit var viewModel: ClientListViewModel
    private lateinit var listAdapter: ClientListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        //Inflate the layout for this fragment
        val binding: FragmentClientListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_client_list, container, false
        )

        activity?.findViewById<ProgressBar>(R.id.progress_bar)!!.visibility = View.VISIBLE

        //Create the View Model
        val viewModelFactory = ClientListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClientListViewModel::class.java)

        //Setting up the recycler view
        listAdapter = ClientListAdapter(ClientListAdapter.ClientListener { client ->
            viewModel.onClientClicked(client) })
        listAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.clientsList.adapter = listAdapter

        viewModel.clients.observe(viewLifecycleOwner) {
            it?.let {
                listAdapter.submitClientList(it)
            }
        }

        viewModel.navigateToAdd.observe(viewLifecycleOwner) {
            if (it) {
                findNavController()
                    .navigate(ClientListFragmentDirections.actionClientListFragmentToClientAddFragment())
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateToInfo.observe(viewLifecycleOwner) { client ->
            client?.let {
                findNavController()
                .navigate(ClientListFragmentDirections.actionClientListFragmentToClientInfoFragment(it))
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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_list, menu)
        //Implement SearchView
        searchOnList(menu.findItem(R.id.action_search))
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_refresh_list -> viewModel.onClickRefreshList()
        }
        return super.onOptionsItemSelected(item)
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