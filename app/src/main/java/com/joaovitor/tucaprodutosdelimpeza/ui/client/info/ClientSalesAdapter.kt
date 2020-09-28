package com.joaovitor.tucaprodutosdelimpeza.ui.client.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemClientSaleBinding

class ClientSalesAdapter(var viewModel: ClientInfoViewModel) : RecyclerView.Adapter<ClientSalesAdapter.ViewHolder>() {
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
        holder.bind(item, viewModel)
    }

    override fun getItemCount() = listData.size

    class ViewHolder private constructor(private val binding: ListItemClientSaleBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Sale, viewModel: ClientInfoViewModel) {
            binding.sale = item
            binding.viewModel = viewModel
            binding.productsList.adapter = ClientSaleProductsAdapter(item.products)
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