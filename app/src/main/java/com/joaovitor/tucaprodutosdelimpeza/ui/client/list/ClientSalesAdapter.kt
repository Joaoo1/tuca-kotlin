package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemClientSaleBinding

class ClientSalesAdapter : RecyclerView.Adapter<ClientSalesAdapter.ViewHolder>() {
    var listData = listOf<Sale>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]
        holder.bind(item)
    }

    override fun getItemCount() = listData.size

    class ViewHolder private constructor(private val binding: ListItemClientSaleBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Sale) {
            binding.sale = item
            binding.productsList.adapter = item.products?.let { ClientSaleProductsAdapter(it) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemClientSaleBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}