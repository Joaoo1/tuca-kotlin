package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemSaleBinding

class SaleListAdapter(val clickListener: SaleListener) : RecyclerView.Adapter<SaleListAdapter.ViewHolder>(){
     var listData = listOf<Sale>()
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
        holder.bind(clickListener, item)
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
}