package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentFilteredSalesBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.info.SaleInfoFragment.Companion.REQUEST_ENABLED_BT
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.list.SaleListAdapter
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class FilteredSalesFragment : Fragment() {

    private lateinit var viewModel: ReportSalesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentFilteredSalesBinding = DataBindingUtil
            .inflate(inflater,R.layout.fragment_filtered_sales,container, false)

        //Create the viewModel
        val viewModelFactory = ReportSalesViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)[ReportSalesViewModel::class.java]

        //Setting up RecyclerView
        val listAdapter = FilteredSalesListAdapter(SaleListAdapter.SaleListener { sale ->
            viewModel.onSaleClicked(sale)
        })
        binding.filteredSales.adapter = listAdapter
        viewModel.filteredSales.observe(viewLifecycleOwner){
            it?.let {
                listAdapter.submitList(it)
            }
        }

        //Navigate to Info Sale Fragment listener
        viewModel.navigateToInfoSale.observe(viewLifecycleOwner){ sale ->
            sale?.let {
                findNavController()
                    .navigate(
                        FilteredSalesFragmentDirections.actionFilteredSalesFragmentToSalesInfoFragment(it)
                    )
                viewModel.doneNavigating()
            }
        }

        viewModel.requestBluetoothOn.observe(viewLifecycleOwner) {
            if(it) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLED_BT)
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

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.print_report, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_print -> {
                        viewModel.onClickPrintReport(requireContext())
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLED_BT) viewModel.onBluetoothResult(resultCode, requireContext())
        super.onActivityResult(requestCode, resultCode, data)
    }
}