package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.databinding.DialogInfoClientBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientListBinding

class ClientListFragment : Fragment() {

    private lateinit var viewModel: ClientListViewModel
    private lateinit var infoClientDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding: FragmentClientListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_client_list, container, false
        )

        val viewModelFactory = ClientListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClientListViewModel::class.java)

        //Setting up the recycler view
        val adapter = ClientListAdapter(ClientListAdapter.ClientListener { client ->
            viewModel.onClientClicked(client)
        })
        binding.clientsList.adapter = adapter

        viewModel.clients.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeadersAndSubmitList(it)
            }
        })
        //Floating button click
        binding.fab.setOnClickListener { viewModel.onClickFab() }

        //Navigate to Add Fragment listener
        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController()
                    .navigate(ClientListFragmentDirections.actionClientListFragmentToClientAddFragment())
                viewModel.doneNavigation()
            }
        })

        //Navigate to Edit Fragment listener
        viewModel.navigateToEdit.observe(viewLifecycleOwner, Observer { client ->
            client?.let {
                findNavController()
                    .navigate(ClientListFragmentDirections.actionClientListFragmentToClientEditFragment(client))
                viewModel.doneNavigation()
            }
        })

        viewModel.openInfoDialog.observe(viewLifecycleOwner, Observer { client ->
            if (client != null) {
                showInfoClientDialog(client)
            } else {
                if (this::infoClientDialog.isInitialized) {
                    infoClientDialog.dismiss()
                }

            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(2)?.isChecked = true
    }

    private fun showInfoClientDialog(client: Client) {
        infoClientDialog = Dialog(requireContext())
        infoClientDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view =
            layoutInflater.inflate(R.layout.dialog_info_client, null, false)

        infoClientDialog.setContentView(view)
        val binding = DialogInfoClientBinding.bind(view)

        binding.client = client

        binding.editClient.setOnClickListener {
            viewModel.openEditClient(client)
        }

       binding.openBuys.setOnClickListener {
            showSalesMade()
        }

        infoClientDialog.show()
    }

    private fun showSalesMade() {
        val showSalesMadeDialog = activity?.let { Dialog(it) }!!
        showSalesMadeDialog.setContentView(R.layout.dialog_client_sales)

        val adapter = ClientSalesAdapter()
        (showSalesMadeDialog.findViewById(R.id.client_sales) as RecyclerView).addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        (showSalesMadeDialog.findViewById(R.id.client_sales) as RecyclerView).adapter = adapter
        (showSalesMadeDialog.findViewById(R.id.headline_client_sales) as TextView).text =
            String.format(resources.getString(R.string.dialog_client_sales_title), "Joaquim")

        /*viewModel.clientSales.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.listData = it
            }
        })*/

        showSalesMadeDialog.show()
    }
}