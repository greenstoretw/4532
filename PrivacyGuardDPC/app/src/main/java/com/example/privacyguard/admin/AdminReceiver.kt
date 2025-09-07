
package com.example.privacyguard.admin

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AdminReceiver : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        Toast.makeText(context, "PrivacyGuard DPC 已啟用", Toast.LENGTH_SHORT).show()
    }
    override fun onDisabled(context: Context, intent: Intent) {
        Toast.makeText(context, "PrivacyGuard DPC 已停用", Toast.LENGTH_SHORT).show()
    }
}
