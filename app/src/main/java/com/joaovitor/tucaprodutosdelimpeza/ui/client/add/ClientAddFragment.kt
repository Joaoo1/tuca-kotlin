package com.joaovitor.tucaprodutosdelimpeza.ui.client.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.joaovitor.tucaprodutosdelimpeza.R

class ClientAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_fragment_client_add)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_add, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}