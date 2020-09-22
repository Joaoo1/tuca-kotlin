package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemClientBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemClientHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.collections.ArrayList

class ClientListAdapter(val clickListener: ClientListener) : ListAdapter<ClientListAdapter.DataItem,
            RecyclerView.ViewHolder>(ClientDiffCallback()), Filterable {

    companion object {
        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private var clientsList = listOf<Client>()

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitClientList(list: List<Client>){
        addHeadersAndSubmitList(list)
        clientsList = list
    }

    private fun addHeadersAndSubmitList(list: List<Client>) {
        adapterScope.launch {
            val listWithHeaders = mutableListOf<DataItem>()
            var lastLetter = ""
            list.forEach {
                val letter = it.name.first().toString()
                if(letter != lastLetter) {
                    listWithHeaders.add(DataItem.Header(letter))
                    lastLetter = letter
                }
                listWithHeaders.add(DataItem.ClientItem(it))
            }
            withContext(Dispatchers.Main) {
                submitList(listWithHeaders)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val headerItem = getItem(position) as DataItem.Header
                holder.bind(headerItem.headerId)
            }
            is ViewHolder -> {
                val clientItem = getItem(position) as DataItem.ClientItem
                holder.bind(clickListener, clientItem.client)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ClientItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderViewHolder private constructor(private val binding: ListItemClientHeaderBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(letter: String) {
            binding.letter = letter
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemClientHeaderBinding.inflate(layoutInflater, parent, false)

                return HeaderViewHolder(binding)
            }
        }
    }

    class ViewHolder private constructor(private val binding: ListItemClientBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ClientListener, item: Client) {
            binding.client = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemClientBinding.inflate(layoutInflater, parent, false)

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
                    resultList.addAll(clientsList)
                } else {
                    for (client in clientsList) {
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
                    addHeadersAndSubmitList(results.values as List<Client>)
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
    class ClientDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    class ClientListener(val clickListener: (client: Client) -> Unit) {
        fun onClick(client: Client) = clickListener(client)
    }

    sealed class DataItem {
        data class ClientItem(val client: Client): DataItem() {
            override val id = client.id
        }

        data class Header(val headerId: String) : DataItem() {
            override val id = headerId
        }

        abstract val id: String
    }
}