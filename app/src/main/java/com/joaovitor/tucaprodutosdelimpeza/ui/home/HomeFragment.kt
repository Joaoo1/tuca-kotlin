package com.joaovitor.tucaprodutosdelimpeza.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val viewModelFactory = HomeViewModelFactory()
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel

        homeViewModel.navigateToProduct.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToProductListFragment())
                homeViewModel.doneNavigating()
            }
        })

        homeViewModel.navigateToClient.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToClientListFragment())
                homeViewModel.doneNavigating()
            }
        })

        homeViewModel.navigateToSale.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSalesListFragment())
                homeViewModel.doneNavigating()
            }
        })

        homeViewModel.navigateToAddSale.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSalesAddFragment())
                homeViewModel.doneNavigating()
            }
        })

        homeViewModel.navigateToReport.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToReportsFragment())
                homeViewModel.doneNavigating()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<NavigationView>(R.id.nav_view)?.menu?.getItem(0)?.isChecked = true
    }
}