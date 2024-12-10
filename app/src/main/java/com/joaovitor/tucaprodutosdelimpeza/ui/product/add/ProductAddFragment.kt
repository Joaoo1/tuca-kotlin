package com.joaovitor.tucaprodutosdelimpeza.ui.product.add

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentProductAddBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toast
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class ProductAddFragment : Fragment() {

    private lateinit var viewModel: ProductAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Create the viewModel
        val viewModelFactory = ProductAddViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductAddViewModel::class.java]

        // Inflate the layout for this fragment
        val binding: FragmentProductAddBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_add, container, false)

        viewModel.navigateBack.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().popBackStack()
                viewModel.doneNavigating()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.product_add, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_save -> {
                        viewModel.onClickSave()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}