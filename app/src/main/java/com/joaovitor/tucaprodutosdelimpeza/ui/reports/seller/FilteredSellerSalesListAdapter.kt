package com.joaovitor.tucaprodutosdelimpeza.ui.reports.seller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemFilteredSaleBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.sale.list.SaleListAdapter

class FilteredSellerSalesListAdapter(val clickListener: SaleListAdapter.SaleListener) :
    ListAdapter<Sale, FilteredSellerSalesListAdapter.ViewHolder>(FilteredSaleDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    class ViewHolder private constructor(private val binding: ListItemFilteredSaleBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SaleListAdapter.SaleListener, item: Sale) {
            binding.sale = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFilteredSaleBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     *
     * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    class FilteredSaleDiffCallback : DiffUtil.ItemCallback<Sale>() {
        override fun areItemsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem == newItem
        }
    }
}