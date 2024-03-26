package com.joaovitor.tucaprodutosdelimpeza.ui.client.info

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentClientInfoBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class ClientInfoFragment : Fragment() {

    companion object {
        const val REQUEST_CALL_PERMISSION = 1
    }

    private lateinit var viewModel: ClientInfoViewModel
    private lateinit var listAdapter: ClientSalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val client = ClientInfoFragmentArgs.fromBundle(requireArguments()).client

        //Inflate the layout for this fragment
        val binding: FragmentClientInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_client_info, container, false)

        //Create the viewModel
        val viewModelFactory = ClientInfoViewModelFactory(client)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClientInfoViewModel::class.java)

        viewModel.navigateToEditClient.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    ClientInfoFragmentDirections.actionClientInfoFragmentToClientEditFragment(it))
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateToInfoSale.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    ClientInfoFragmentDirections.actionClientInfoFragmentToSalesInfoFragment(it))
                viewModel.doneNavigating()
            }
        }

        //Setting up the recycler view
        listAdapter = ClientSalesAdapter(viewModel)
        binding.clientSales.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        listAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.clientSales.adapter = listAdapter

        viewModel.clientSales.observe(viewLifecycleOwner) {
            it?.let {
                listAdapter.listData = it
            }
        }

        viewModel.callClientPhone.observe(viewLifecycleOwner) {
            it?.let {
                startActivity(it)
                viewModel.doneCalling()
            }
        }

        viewModel.sendWhatsappClient.observe(viewLifecycleOwner) {
            it?.let {
                startActivity(it)
                viewModel.doneSendWhatsapp()
            }
        }

        viewModel.requestCallPermission.observe(viewLifecycleOwner) {
            if(it) {
                requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> viewModel.onClickEditClient()
            R.id.action_call -> viewModel.onClickCallClient(requireContext())
            R.id.action_whatsapp -> viewModel.onClickWhatsapp(requireContext())
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CALL_PERMISSION -> viewModel.onRequestCallPermissionResult(grantResults)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}