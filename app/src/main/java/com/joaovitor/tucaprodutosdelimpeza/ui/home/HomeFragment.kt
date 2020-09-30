package com.joaovitor.tucaprodutosdelimpeza.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        val viewModelFactory = HomeViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.navigateToProduct.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToProductListFragment())
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateToClient.observe(viewLifecycleOwner, Observer {
            if(it) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToClientListFragment())
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateToSale.observe(viewLifecycleOwner, Observer {
            if(it) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSalesListFragment())
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateToAddSale.observe(viewLifecycleOwner, Observer {
            if(it) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSalesAddFragment())
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateToReport.observe(viewLifecycleOwner, Observer {
            if(it) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToReportsFragment())
                viewModel.doneNavigating()
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(0)?.isChecked = true
    }
}