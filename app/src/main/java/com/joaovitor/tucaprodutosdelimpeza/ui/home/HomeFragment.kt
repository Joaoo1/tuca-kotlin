package com.joaovitor.tucaprodutosdelimpeza.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.FragmentHomeBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.client.ClientActivity
import com.joaovitor.tucaprodutosdelimpeza.ui.product.ProductActivity
import com.joaovitor.tucaprodutosdelimpeza.ui.reports.ReportActivity
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.SaleActivity

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val viewModelFactory = HomeViewModelFactory();
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel

        homeViewModel.navigateToProduct.observe(viewLifecycleOwner, Observer {
            if (it == true) {
//                val intent = Intent(activity, ProductActivity::class.java)
//                startActivity(intent)
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToProductListFragment())
                homeViewModel.doneNavigating()
            }
        })

        homeViewModel.navigateToClient.observe(viewLifecycleOwner, Observer {
            if(it == true) {
               /* val intent = Intent(activity, ClientActivity::class.java)
                startActivity(intent)*/
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToClientListFragment())
                homeViewModel.doneNavigating()
            }
        })

        homeViewModel.navigateToSale.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                /*val intent = Intent(activity, SaleActivity::class.java)
                startActivity(intent)*/
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSalesListFragment())
                homeViewModel.doneNavigating()
            }
        })

        homeViewModel.navigateToReport.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                /*val intent = Intent(activity, ReportActivity::class.java)
                startActivity(intent)*/
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToReportSalesFragment())
                homeViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}