package com.joaovitor.tucaprodutosdelimpeza.ui.client.info

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientInfoBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.client.add.ClientAddFragmentDirections
import com.joaovitor.tucaprodutosdelimpeza.ui.client.add.ClientAddViewModel
import com.joaovitor.tucaprodutosdelimpeza.ui.client.add.ClientAddViewModelFactory
import com.joaovitor.tucaprodutosdelimpeza.ui.client.list.ClientListAdapter

class ClientInfoFragment : Fragment() {

    private lateinit var viewModel: ClientInfoViewModel
    private lateinit var listAdapter: ClientSalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val client = ClientInfoFragmentArgs.fromBundle(requireArguments()).client

        //Create the viewModel
        val viewModelFactory = ClientInfoViewModelFactory(client)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClientInfoViewModel::class.java)

        viewModel.navigateToEditClient.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    ClientInfoFragmentDirections.actionClientInfoFragmentToClientEditFragment(it))
                viewModel.doneNavigation()
            }
        })

        viewModel.navigateToInfoSale.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    ClientInfoFragmentDirections.actionClientInfoFragmentToSalesInfoFragment(it))
                viewModel.doneNavigation()
            }
        })

        //Inflate the layout for this fragment
        val binding: FragmentClientInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_client_info, container, false)

        //Setting up the recycler view
        listAdapter = ClientSalesAdapter(viewModel)
        binding.clientSales.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.clientSales.adapter = listAdapter
        viewModel.clientSales.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.listData = it
            }
        })

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_edit -> viewModel.onClickEditClient()
            //R.id.action_call -> viewModel.onClickCallClient() TODO: Implement call phone
        }
        return super.onOptionsItemSelected(item)
    }
}