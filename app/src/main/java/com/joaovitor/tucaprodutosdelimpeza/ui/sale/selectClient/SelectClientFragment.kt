package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

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
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSelectClientBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.client.list.ClientListAdapter
//import com.joaovitor.tucaprodutosdelimpeza.ui.sale.list.SalesListFragmentDirections

class SelectClientFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSelectClientBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_select_client, container, false)

        val viewModelFactory = SelectClientViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SelectClientViewModel::class.java)
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

        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer {
            if(it) {
                this.findNavController()
                    .popBackStack()
                onDestroy()
            }
        })

        return binding.root
    }

}