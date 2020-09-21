package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.databinding.ListItemSaleBinding
import java.util.*
import kotlin.collections.ArrayList


class SaleListAdapter(val clickListener: SaleListener) :
    RecyclerView.Adapter<SaleListAdapter.ViewHolder>(), Filterable{
     var saleList = listOf<Sale>()
        set(value) {
            field = value
            saleFilterList = value
            notifyDataSetChanged()
        }

    var saleFilterList = listOf<Sale>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = saleFilterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = saleFilterList[position]
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    saleFilterList = saleList
                } else {
                    var resultList: List<Sale> = ArrayList()
                    for (sale in saleList) {
                        if (sale.saleId
                                .toString()
                                .toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList = resultList.plus(sale)
                        }
                    }
                    saleFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = saleFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if(results?.values != null) {
                    saleFilterList = results.values as List<Sale>
                    println(results.values)
                }
                    notifyDataSetChanged()
            }
        }
    }
}