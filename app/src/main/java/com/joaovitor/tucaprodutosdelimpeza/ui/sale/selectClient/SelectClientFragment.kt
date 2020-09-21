package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import android.os.Bundle
import android.view.*
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
        val adapter = SelectClientListAdapter(SelectClientListAdapter.SelectClientListener { client ->
            viewModel.onClientClicked(client)
        })
        binding.clientsList.adapter = adapter
        viewModel.allClients.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.listData = it
            }
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
        super.onCreateOptionsMenu(menu, inflater)
    }
}