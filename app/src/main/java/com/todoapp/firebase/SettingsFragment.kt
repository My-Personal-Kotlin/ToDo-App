
package com.todoapp.firebase

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.firebase.ui.auth.AuthUI
import com.todoapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        const val TAG = "SettingsFragment"
    }

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(TAG, "Authenticated")
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    Log.i(TAG, "Unauthenticated")
//                    findNavController().navigate(R.id.loginFragment)
                }
                else -> {
                    Log.e(
                        TAG, "New $authenticationState state that doesn't require any UI change"
                    )
                }
            }
        })
    }
}
