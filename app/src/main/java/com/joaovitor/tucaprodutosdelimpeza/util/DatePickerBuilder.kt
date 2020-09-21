package com.joaovitor.tucaprodutosdelimpeza.util

import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.util.Calendar

object DatePickerBuilder {

    fun buildDatePicker(clickListener: MaterialPickerOnPositiveButtonClickListener<Long>,
                        title: String? = "Selecione uma data"): MaterialDatePicker<Long>{
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText(title)
        val date = Calendar.getInstance()
        date.add(Calendar.HOUR, 3)
        builder.setSelection(date.timeInMillis)
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener(clickListener)

        return picker
    }
}