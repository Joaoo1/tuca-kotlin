package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleInfoBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class SaleInfoFragment : Fragment() {

    companion object {
        const val REQUEST_ENABLED_BT = 1
    }

    private lateinit var sale: Sale
    private lateinit var viewModel: SaleInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        sale = arguments?.let { SaleInfoFragmentArgs.fromBundle(it).sale }!!
        activity?.title = String.format(
            resources.getString(R.string.title_fragment_sale_info),
            sale.saleId
        )

        // Inflate the layout for this fragment
        val binding: FragmentSaleInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_info, container, false
        )

        val adapter = SaleInfoListAdapter()
        binding.productsList.adapter = adapter

        //Create the viewModel
        val viewModelFactory = SaleInfoViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(SaleInfoViewModel::class.java)
        
        viewModel.setSale(sale)

        viewModel.sale.observe(viewLifecycleOwner) {
            it?.let {
                adapter.listData = it.products
            }
        }

        viewModel.navigateToEditProducts.observe(viewLifecycleOwner) {
            if(it) {
                findNavController()
                    .navigate(
                        SaleInfoFragmentDirections.actionSalesInfoFragmentToSaleEditProductsFragment(
                            sale
                        )
                    )
                viewModel.doneNavigating()
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().popBackStack()
                viewModel.doneNavigating()
            }
        }

        viewModel.openPaymentDialog.observe(viewLifecycleOwner) {
            if(it) {
                createPaymentDialog()
                viewModel.doneNavigating()
            }
        }

        viewModel.openDeleteDialog.observe(viewLifecycleOwner) {
            if(it) {
                createDeleteDialog()
                viewModel.doneNavigating()
            }
        }

        viewModel.requestBluetoothOn.observe(viewLifecycleOwner) {
            if(it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestMultiplePermissions.launch(arrayOf(
                        BLUETOOTH_SCAN, BLUETOOTH_CONNECT))
                }
                else{
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    requestBluetooth.launch(enableBtIntent)
                }
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    /* Bluetooth permissions */
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test_PERMISSIONS", "${it.key} = ${it.value}")
            }
        }

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            context?.toast("Imprima novamente.")
        }else{
            context?.toastLong("Permissão recusada. Não é possível imprimir.")
        }
    }
    /* End bluetooth permissions */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_register_payment -> viewModel.onClickRegisterPayment()
            R.id.action_delete_sale -> viewModel.onClickDeleteSale()
            R.id.action_edit_products -> viewModel.onClickEditProducts()
            R.id.action_print -> viewModel.onClickPrintReceipt(requireContext())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createPaymentDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_payment, null)
        val textInput: TextInputLayout = view.findViewById(R.id.paid_value)
        textInput.helperText = String.format(
            resources
                .getString(R.string.dialog_payment_paid_value_helper_text), sale.toReceive
        )

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(view)
                .setTitle(getString(R.string.dialog_payment_title))
                .setNegativeButton(getString(R.string.dialog_payment_negative_button))
                    { _, _ -> viewModel.doneNavigating() }
                .setPositiveButton(getString(R.string.dialog_payment_positive_button))
                    { _, _ -> viewModel.registerPayment(textInput.editText!!.text.toString()) }
                .show()
        }
    }

    private fun createDeleteDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.dialog_delete_sale_title))
                .setMessage(getString(R.string.dialog_delete_sale_message))
                .setNegativeButton(getString(R.string.dialog_delete_sale_negative_button), null)
                .setPositiveButton(getString(R.string.dialog_delete_sale_positive_button)) { _, _ -> viewModel.deleteSale() }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLED_BT) viewModel.onBluetoothResult(resultCode, requireContext())
        super.onActivityResult(requestCode, resultCode, data)
    }
}