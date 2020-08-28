package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemClientBinding

class ClientListAdapter(val clickListener: ClientListener) : RecyclerView.Adapter<ClientListAdapter.ViewHolder>(){
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

    class ClientListener(val clickListener: (client: Client) -> Unit) {
        fun onClick(client: Client) = clickListener(client)
    }
}