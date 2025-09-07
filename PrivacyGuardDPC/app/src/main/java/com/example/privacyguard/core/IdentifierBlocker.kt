
package com.example.privacyguard.core

import android.app.AppOpsManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.example.privacyguard.admin.AdminReceiver

class IdentifierBlocker(private val context: Context) {

    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val admin = ComponentName(context, AdminReceiver::class.java)
    private val pm = context.packageManager

    private val dangerousPerms = listOf(
        android.Manifest.permission.READ_PHONE_STATE
    )

    private val ops = listOf(
        AppOpsCompat.OPSTR_READ_PHONE_STATE,
        AppOpsCompat.OPSTR_READ_DEVICE_IDENTIFIERS
    )

    fun enforceForPackages(blockList: Set<String>, allowList: Set<String>) {
        check(dpm.isDeviceOwnerApp(context.packageName)) { "非 Device Owner 無法套用全域策略" }
        val toBlock = blockList - allowList
        toBlock.forEach { pkg -> applyForPackage(pkg) }
    }

    fun enforceInstagramOnly() {
        check(dpm.isDeviceOwnerApp(context.packageName)) { "非 Device Owner 無法套用全域策略" }
        applyForPackage("com.instagram.android")
    }

    private fun applyForPackage(pkg: String) {
        // 1) 危險權限 → DENY
        dangerousPerms.forEach { perm ->
            try {
                dpm.setPermissionGrantState(
                    admin, pkg, perm,
                    DevicePolicyManager.PERMISSION_GRANT_STATE_DENIED
                )
            } catch (_: Throwable) { }
        }
        // 2) AppOps → MODE_IGNORED
        try {
            val ai: ApplicationInfo = pm.getApplicationInfo(pkg, 0)
            ops.forEach { op ->
                AppOpsCompat.setUidIgnored(context, op, ai.uid, pkg)
            }
        } catch (_: Throwable) { }
    }
}
