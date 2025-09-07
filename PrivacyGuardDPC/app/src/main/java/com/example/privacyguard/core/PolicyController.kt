
package com.example.privacyguard.core

import android.content.Context
import androidx.work.*
import com.example.privacyguard.util.Persist
import java.util.concurrent.TimeUnit

object PolicyController {

    fun applyNow(context: Context) {
        val block = Persist.loadBlocklist(context)
        val allow = Persist.loadAllowlist(context)
        IdentifierBlocker(context).enforceForPackages(block, allow)
    }

    fun applyInstagramNow(context: Context) {
        IdentifierBlocker(context).enforceInstagramOnly()
    }

    fun schedulePeriodicReapply(context: Context) {
        val req = PeriodicWorkRequestBuilder<ReapplyWorker>(12, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "policy_reapply", ExistingPeriodicWorkPolicy.UPDATE, req
        )
    }

    class ReapplyWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
        override suspend fun doWork(): Result {
            applyNow(applicationContext)
            return Result.success()
        }
    }
}
