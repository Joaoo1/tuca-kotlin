package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.joaovitor.tucaprodutosdelimpeza.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val preferenceManageAddress: Preference? = findPreference("manage_address")
        preferenceManageAddress?.setOnPreferenceClickListener{
            this.findNavController()
                .navigate(SettingsFragmentDirections.actionSettingsFragmentToManageAddressFragment())
            true
        }

    }
}