package com.joaovitor.tucaprodutosdelimpeza.util

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import java.math.BigDecimal
import java.util.Date

@BindingAdapter("formatDateToString")
fun formatDateToString(textView: TextView, date: Date) {
    val formattedDate =  FormatDate.formatDateToString(date)
    @Suppress("UsePropertyAccessSyntax")
    textView.setText(formattedDate)
}

@BindingAdapter("getSaleSituation")
fun getSaleSituation(textView: MaterialTextView, sale: Sale) {
    if (sale.paid!!) {
        textView.text = textView.resources.getString(R.string.sale_paid)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.colorPaid))
        return
    }

    if (BigDecimal(sale.paidValue) == BigDecimal("0.00")) {
        textView.text = textView.resources.getString(R.string.sale_unpaid)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.colorUnpaid))
        return
    }

    textView.text = textView.resources.getString(R.string.sale_partially_paid)
    textView.setTextColor(ContextCompat.getColor(textView.context, R.color.colorPartiallyPaid))
}