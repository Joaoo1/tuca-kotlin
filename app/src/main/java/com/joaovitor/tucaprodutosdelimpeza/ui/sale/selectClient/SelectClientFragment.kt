package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_fragment_sale_select_client)

        // Inflate the layout for this fragment
        val binding: FragmentSelectClientBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_select_client, container, false)

        val viewModelFactory = SelectClientViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SelectClientViewModel::class.java)
        val adapter = SelectClientListAdapter(SelectClientListAdapter.SelectClientListener { client ->
            viewModel.onClientClicked(client)
        })
        binding.clientsList.adapter = adapter

        viewModel.setClients()

        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer {
            if(it) {
                this.findNavController()
                    .popBackStack()
                onDestroy()
            }
        })

        viewModel.clients.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.listData = it
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}