
package com.example.privacyguard.provisioning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.privacyguard.core.PolicyController
import com.example.privacyguard.util.Persist

class ProvisioningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 預載：只鎖 Instagram；白名單可擴充（例如各家 VPN）
        Persist.saveBlocklist(this, setOf("com.instagram.android"))
        Persist.saveAllowlist(this, setOf(
            "net.openvpn.openvpn",
            "com.paloaltonetworks.globalprotect",
            "com.cisco.anyconnect.vpn.android.avf",
            "com.expressvpn.vpn",
            "com.nordvpn.android"
        ))
        PolicyController.applyNow(this)
        PolicyController.schedulePeriodicReapply(this)
        finish()
    }
}
