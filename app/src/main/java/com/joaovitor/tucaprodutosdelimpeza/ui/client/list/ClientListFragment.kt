package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientListBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductListBinding

/**
 * A fragment representing a list of Items.
 */
class ClientListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentClientListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_client_list, container, false)
        val viewModelFactory = ClientListViewModelFactory()
        val viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ClientListViewModel::class.java)
        //Setting up the recycler view
        val adapter = ClientListAdapter(ClientListAdapter.ClientListener { client ->
            viewModel.onClientClicked(client)
        })
        binding.clientsList.adapter = adapter

        viewModel.clients.observe(viewLifecycleOwner, Observer {
            it?.let {
                (binding.clientsList.adapter as ClientListAdapter).listData = it
            }
        })
        viewModel.setClients()

        //Floating button click
        binding.fab.setOnClickListener { viewModel.onClickFab() }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer { navigate ->
            if(navigate) {
                findNavController()
                    .navigate(ClientListFragmentDirections.actionClientListFragmentToClientAddFragment())
                viewModel.doneNavigation()
            }
        })

        //Navigate to Edit Fragment listener
        viewModel.navigateToEdit.observe(viewLifecycleOwner, Observer { client ->
            client?.let {
                findNavController()
                    .navigate(ClientListFragmentDirections.actionClientListFragmentToClientEditFragment())
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }
}