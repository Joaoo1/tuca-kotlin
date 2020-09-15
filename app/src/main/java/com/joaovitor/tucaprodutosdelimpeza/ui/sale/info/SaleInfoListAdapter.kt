package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemInfoSaleProductBinding

class SaleInfoListAdapter() : RecyclerView.Adapter<SaleInfoListAdapter.ViewHolder>(){
    var listData = listOf<ProductSale>()
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

    class ViewHolder private constructor(private val binding: ListItemInfoSaleProductBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductSale) {
            binding.product = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemInfoSaleProductBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}