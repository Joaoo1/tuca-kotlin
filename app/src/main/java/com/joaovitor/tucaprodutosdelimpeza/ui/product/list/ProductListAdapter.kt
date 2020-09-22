package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemProductBinding
import java.util.Locale
import kotlin.collections.ArrayList

class ProductListAdapter(val clickListener: ProductListener) :
    ListAdapter<Product, ProductListAdapter.ViewHolder>(ProductDiffCallback()), Filterable{

    var productsList = listOf<Product>()
        set(value) {
            submitList(value)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    class ViewHolder private constructor(private val binding: ListItemProductBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ProductListener, item: Product) {
            binding.product = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemProductBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class ProductListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList: MutableList<Product> = ArrayList()

                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultList.addAll(productsList)
                } else {
                    for (product in productsList) {
                        if (product.name
                                .toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(product)
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
                    submitList(results.values as List<Product>)
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
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}