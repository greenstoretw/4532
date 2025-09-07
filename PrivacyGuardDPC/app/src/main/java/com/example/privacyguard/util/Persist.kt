
package com.example.privacyguard.util

import android.content.Context
import android.content.SharedPreferences

object Persist {
    private const val PREF = "pg_prefs"
    private const val KEY_BLOCK = "blocklist"
    private const val KEY_ALLOW = "allowlist"

    private fun sp(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun saveBlocklist(ctx: Context, pkgs: Set<String>) {
        sp(ctx).edit().putStringSet(KEY_BLOCK, pkgs).apply()
    }
    fun loadBlocklist(ctx: Context): Set<String> =
        sp(ctx).getStringSet(KEY_BLOCK, emptySet()) ?: emptySet()

    fun saveAllowlist(ctx: Context, pkgs: Set<String>) {
        sp(ctx).edit().putStringSet(KEY_ALLOW, pkgs).apply()
    }
    fun loadAllowlist(ctx: Context): Set<String> =
        sp(ctx).getStringSet(KEY_ALLOW, emptySet()) ?: emptySet()
}
