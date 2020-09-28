package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemSelectClientBinding
import java.util.*
import kotlin.collections.ArrayList

class SelectClientListAdapter(val clickListener: SelectClientListener)
    : ListAdapter<Client, SelectClientListAdapter.ViewHolder>(ClientDiffCallback()), Filterable{

    var allClients = listOf<Client>()
        set(value) {
            field = value
            submitList(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    class ViewHolder private constructor(private val binding: ListItemSelectClientBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SelectClientListener, item: Client) {
            binding.client = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSelectClientBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList: MutableList<Client> = ArrayList()

                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultList.addAll(allClients)
                } else {
                    for (client in allClients) {
                        if (client.name
                                .toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(client)
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
                    submitList(results.values as List<Client>)
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
    class ClientDiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem == newItem
        }
    }

    class SelectClientListener(val clickListener: (client: Client) -> Unit) {
        fun onClick(product: Client) = clickListener(product)
    }
}