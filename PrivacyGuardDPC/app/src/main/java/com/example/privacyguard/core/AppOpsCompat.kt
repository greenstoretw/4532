
package com.example.privacyguard.core

import android.app.AppOpsManager
import android.content.Context
import android.os.Build

object AppOpsCompat {
    const val OPSTR_READ_PHONE_STATE = AppOpsManager.OPSTR_READ_PHONE_STATE
    const val OPSTR_READ_DEVICE_IDENTIFIERS = "android:read_device_identifiers"

    fun setUidIgnored(context: Context, op: String, uid: Int, packageName: String?) {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                appOps.setUidMode(op, uid, AppOpsManager.MODE_IGNORED)
            } else {
                val m = AppOpsManager::class.java.getMethod(
                    "setMode",
                    String::class.java, Int::class.javaPrimitiveType,
                    String::class.java, Int::class.javaPrimitiveType
                )
                m.invoke(appOps, op, uid, packageName, AppOpsManager.MODE_IGNORED)
            }
        } catch (_: Throwable) { }
    }
}
