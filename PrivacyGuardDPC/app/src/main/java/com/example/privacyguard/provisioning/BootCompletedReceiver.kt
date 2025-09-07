
package com.example.privacyguard.provisioning

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.privacyguard.core.PolicyController

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        PolicyController.applyNow(context)
        PolicyController.schedulePeriodicReapply(context)
    }
}
