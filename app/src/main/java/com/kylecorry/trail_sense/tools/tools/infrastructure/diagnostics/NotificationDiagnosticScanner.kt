package com.kylecorry.trail_sense.tools.tools.infrastructure.diagnostics

import android.content.Context
import com.kylecorry.andromeda.notify.Notify
import com.kylecorry.luna.text.slugify
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.tools.diagnostics.domain.Severity
import com.kylecorry.trail_sense.tools.tools.infrastructure.ToolDiagnosticAction
import com.kylecorry.trail_sense.tools.tools.infrastructure.ToolDiagnosticResult
import com.kylecorry.trail_sense.tools.tools.infrastructure.ToolDiagnosticScanner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NotificationDiagnosticScanner(
    private val channelId: String,
    private val channelName: String
) : ToolDiagnosticScanner {
    override fun quickScan(context: Context): List<ToolDiagnosticResult> {
        return if (Notify.isChannelBlocked(context, channelId)) {
            listOf(
                ToolDiagnosticResult(
                    "notification-channel-blocked-${channelId.slugify()}",
                    Severity.Warning,
                    channelName,
                    context.getString(R.string.notifications_blocked),
                    context.getString(
                        R.string.unblock_notification_channel,
                        channelName
                    ),
                    ToolDiagnosticAction.notification(context, channelId)
                )
            )
        } else {
            emptyList()
        }
    }

    override fun fullScan(context: Context): Flow<List<ToolDiagnosticResult>> {
        return flowOf(quickScan(context))
    }
}