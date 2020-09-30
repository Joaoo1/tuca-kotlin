package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSold
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemProductBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemProductSoldBinding
import java.util.Locale
import kotlin.collections.ArrayList

class ProductsSoldListAdapter :
    ListAdapter<ProductSold, ProductsSoldListAdapter.ViewHolder>(ProductSoldDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ListItemProductSoldBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductSold) {
            binding.productSold = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemProductSoldBinding.inflate(layoutInflater, parent, false)

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
    class ProductSoldDiffCallback : DiffUtil.ItemCallback<ProductSold>() {
        override fun areItemsTheSame(oldItem: ProductSold, newItem: ProductSold): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ProductSold, newItem: ProductSold): Boolean {
            return oldItem == newItem
        }
    }
}