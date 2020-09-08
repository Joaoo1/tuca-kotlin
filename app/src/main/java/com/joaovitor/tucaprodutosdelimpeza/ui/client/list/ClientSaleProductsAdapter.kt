package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemClientSaleProductBinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ClientSaleProductsAdapter(products: List<Product>) : RecyclerView.Adapter<ClientSaleProductsAdapter.ViewHolder>() {
    var listData = listOf<Product>()

    init {
        listData = products
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]
        holder.bind(item)
    }

    override fun getItemCount() = listData.size

    class ViewHolder private constructor(private val binding: ListItemClientSaleProductBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            binding.product = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemClientSaleProductBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}