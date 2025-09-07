
package com.example.privacyguard.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PackageChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        PolicyController.applyNow(context)
    }
}
