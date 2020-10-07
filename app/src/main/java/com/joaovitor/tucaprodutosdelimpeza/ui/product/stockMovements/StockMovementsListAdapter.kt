package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockMovements

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.StockMovement
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemStockHistoryBinding

//FIXME: Use ListAdapter with DiffCallback
class StockMovementsListAdapter : RecyclerView.Adapter<StockMovementsListAdapter.ViewHolder>(){
     var listData = listOf<StockMovement>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ListItemStockHistoryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockMovement) {
            binding.stockMovement = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemStockHistoryBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}