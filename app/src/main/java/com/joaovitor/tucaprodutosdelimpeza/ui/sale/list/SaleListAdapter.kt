package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemSaleBinding
import com.joaovitor.tucaprodutosdelimpeza.ui.client.list.ClientListAdapter
import java.util.*
import kotlin.collections.ArrayList


class SaleListAdapter(val clickListener: SaleListener) :
    ListAdapter<Sale, RecyclerView.ViewHolder>(SaleDiffCallback()), Filterable {

     var saleList = listOf<Sale>()
        set(value) {
            field = value
            submitList(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ViewHolder).bind(clickListener, item)
    }

    class ViewHolder private constructor(private val binding: ListItemSaleBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SaleListener, item: Sale) {
            binding.sale = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSaleBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class SaleListener(val clickListener: (sale: Sale) -> Unit) {
        fun onClick(sale: Sale) = clickListener(sale)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList: MutableList<Sale> = ArrayList()

                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultList.addAll(saleList)
                } else {
                    for (sale in saleList) {
                        if (sale.saleId
                                .toString()
                                .toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(sale)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if(results?.values != null) {
                    submitList(results.values as List<Sale>)
                }
            }
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     *
     * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    class SaleDiffCallback : DiffUtil.ItemCallback<Sale>() {
        override fun areItemsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem == newItem
        }
    }
}