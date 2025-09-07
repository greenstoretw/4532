
package com.example.privacyguard.ui

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.privacyguard.R

class AppPickerAdapter(
    private val apps: List<ApplicationInfo>,
    private val pm: PackageManager,
    private val selected: MutableSet<String>
) : RecyclerView.Adapter<AppPickerAdapter.VH>() {

    inner class VH(v: View): RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.icon)
        val name: TextView = v.findViewById(R.id.name)
        val pkg: TextView = v.findViewById(R.id.pkg)
        val cb: CheckBox = v.findViewById(R.id.cb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_app_select, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val ai = apps[pos]
        h.icon.setImageDrawable(pm.getApplicationIcon(ai))
        h.name.text = pm.getApplicationLabel(ai)
        h.pkg.text = ai.packageName
        h.cb.setOnCheckedChangeListener(null)
        h.cb.isChecked = selected.contains(ai.packageName)
        h.cb.setOnCheckedChangeListener { _, checked ->
            if (checked) selected.add(ai.packageName) else selected.remove(ai.packageName)
        }
    }

    override fun getItemCount(): Int = apps.size
    fun getSelectedPackages(): Set<String> = selected
}
