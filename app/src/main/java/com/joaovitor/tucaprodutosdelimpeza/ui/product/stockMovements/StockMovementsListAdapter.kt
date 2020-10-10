package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockMovements

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.StockMovement
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemStockHistoryChangeBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemStockHistoryMovementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//FIXME: Use ListAdapter with DiffCallback
class StockMovementsListAdapter : ListAdapter<StockMovementsListAdapter.DataItem,
        RecyclerView.ViewHolder>(StockMovementDiffCallback()){

    companion object {
        private const val ITEM_VIEW_TYPE_CHANGE = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)


    fun submitStockMovementsList(list: List<StockMovement>){
        adapterScope.launch {
            val formattedList = mutableListOf<StockMovementsListAdapter.DataItem>()

            list.forEach {
                if(it.isStockChange!!) {
                    formattedList.add(DataItem.StockChange(it))
                } else {
                    formattedList.add(DataItem.StockItem(it))
                }
            }
            withContext(Dispatchers.Main) {
                submitList(formattedList)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.StockChange -> ITEM_VIEW_TYPE_CHANGE
            is DataItem.StockItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_CHANGE -> ChangeViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChangeViewHolder -> {
                val stockChange = getItem(position) as DataItem.StockChange
                holder.bind(stockChange.stock)
            }
            is ItemViewHolder -> {
                val stockItem = getItem(position) as DataItem.StockItem
                holder.bind(stockItem.stock)
            }
        }
    }

    class ItemViewHolder private constructor(private val binding: ListItemStockHistoryMovementBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockMovement) {
            binding.stockMovement = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemStockHistoryMovementBinding.inflate(layoutInflater, parent, false)

                return ItemViewHolder(binding)
            }
        }
    }

    class ChangeViewHolder private constructor(private val binding: ListItemStockHistoryChangeBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockMovement) {
            binding.stockMovement = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemStockHistoryChangeBinding.inflate(layoutInflater, parent, false)

                return ChangeViewHolder(binding)
            }
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     *
     * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    class StockMovementDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class DataItem {
        data class StockItem(var stock: StockMovement): DataItem() {
            override val id = stock.toString()
        }

        data class StockChange(val stock: StockMovement) : DataItem() {
            override val id = stock.toString()
        }

        abstract val id: String
    }
}