package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemClientBinding
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemSelectClientBinding

class SelectClientListAdapter(val clickListener: SelectClientListener) : RecyclerView.Adapter<SelectClientListAdapter.ViewHolder>(){
     var listData = listOf<Client>()
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

    class SelectClientListener(val clickListener: (client: Client) -> Unit) {
        fun onClick(product: Client) = clickListener(product)
    }
}