package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemProductBinding

class ProductListAdapter(val clickListener: ProductListener) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>(){
     var listData = listOf<Product>()
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
}