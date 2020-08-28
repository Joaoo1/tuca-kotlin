package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager?, p: Product?
) :
    FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            ProductEditCadasterFragment()
        } else {
            ProductEditStockFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.product_edit_tab_cadaster, R.string.product_edit_tab_stock)
    }
}