package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentSaleAddBinding
import java.util.Calendar

class SaleAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_fragment_sale_add)
        // Inflate the layout for this fragment
        val binding: FragmentSaleAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sale_add, container, false
        )

        val viewModelFactory = SaleAddViewModelFactory()
        val viewModel: SaleAddViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SaleAddViewModel::class.java)

        binding.viewModel = viewModel

        binding.client.setEndIconOnClickListener {
            viewModel.navigateToSelectClient()
        }

        binding.date.setEndIconOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Selecione a data da venda")
            builder.setSelection(Calendar.getInstance().timeInMillis)
            val picker = builder.build()
            picker.show(parentFragmentManager, "asd")
        }

        binding.radioButtonPartiallyPaid.setOnCheckedChangeListener { _, isChecked ->
            val visibility = if(isChecked) View.VISIBLE else View.GONE
            binding.paidValue.visibility = visibility
        }

        viewModel.navigateToSelectClient.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController()
                    .navigate(SaleAddFragmentDirections.actionSalesAddFragmentToSelectClientFragment())
                viewModel.doneNavigation()
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sale_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save_sale -> createBluetoothDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createBluetoothDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_activate_bluetooth_title))
                .setMessage(getString(R.string.dialog_activate_bluetooth_message))
                .setNeutralButton(getString(R.string.dialog_activate_bluetooth_cancel_button), null)
                .setNegativeButton(
                    getString(R.string.dialog_activate_bluetooth_negative_button),
                    null
                )
                .setPositiveButton(
                    getString(R.string.dialog_activate_bluetooth_positive_button),
                    null
                )
                .show()
        }
    }
}