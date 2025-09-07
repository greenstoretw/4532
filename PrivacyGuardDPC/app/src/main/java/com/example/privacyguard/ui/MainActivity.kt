
package com.example.privacyguard.ui

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.privacyguard.R
import com.example.privacyguard.core.PolicyController
import com.example.privacyguard.util.Persist

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: AppPickerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        check(dpm.isDeviceOwnerApp(packageName)) {
            "請先將本 App 設為 Device Owner 後再操作"
        }

        val rv = findViewById<RecyclerView>(R.id.rvApps)
        val btnApply = findViewById<Button>(R.id.btnApply)
        val btnPick = findViewById<Button>(R.id.btnOpenPicker)
        val btnIgOnly = findViewById<Button>(R.id.btnApplyIgOnly)

        btnIgOnly.setOnClickListener {
            PolicyController.applyInstagramNow(this)
        }

        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
            .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 } // 只列第三方
            .sortedBy { pm.getApplicationLabel(it).toString().lowercase() }

        adapter = AppPickerAdapter(apps, pm, Persist.loadBlocklist(this).toMutableSet())
        rv.adapter = adapter

        btnPick.setOnClickListener {
            rv.visibility = View.VISIBLE
            btnApply.visibility = View.VISIBLE
        }

        btnApply.setOnClickListener {
            Persist.saveBlocklist(this, adapter.getSelectedPackages())
            PolicyController.applyNow(this)
        }

        PolicyController.schedulePeriodicReapply(this)
    }
}
