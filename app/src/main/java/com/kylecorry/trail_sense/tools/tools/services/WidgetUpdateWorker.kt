package com.kylecorry.trail_sense.tools.tools.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kylecorry.andromeda.background.IPeriodicTaskScheduler
import com.kylecorry.andromeda.background.PeriodicTaskSchedulerFactory
import com.kylecorry.andromeda.core.system.Wakelocks
import com.kylecorry.andromeda.core.tryOrLog
import com.kylecorry.andromeda.permissions.Permissions
import com.kylecorry.trail_sense.shared.ParallelCoroutineRunner
import com.kylecorry.trail_sense.shared.permissions.canGetLocationCustom
import com.kylecorry.trail_sense.shared.sensors.LocationSubsystem
import com.kylecorry.trail_sense.shared.sensors.SensorSubsystem
import com.kylecorry.trail_sense.shared.sensors.SensorSubsystem.SensorRefreshPolicy
import com.kylecorry.trail_sense.shared.widgets.WidgetUtils
import com.kylecorry.trail_sense.tools.tools.infrastructure.Tools
import java.time.Duration

class WidgetUpdateWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val wakelock = Wakelocks.get(applicationContext, "WidgetUpdateWorker")
        wakelock?.acquire(Duration.ofSeconds(30).toMillis())
        try {
            // Update stale location/elevation data if needed
            tryOrLog {
                updateStaleSensorData()
            }

            // Update all widgets
            Tools.getTools(applicationContext).flatMap { it.widgets }.forEach {
                WidgetUtils.triggerUpdate(applicationContext, it.widgetClass)
            }
        } finally {
            wakelock?.release()
        }
        return Result.success()
    }

    private suspend fun updateStaleSensorData() {
        val locationSubsystem = LocationSubsystem.getInstance(applicationContext)
        val sensorSubsystem = SensorSubsystem.getInstance(applicationContext)

        if (Permissions.canGetLocationCustom(applicationContext)) {
            val parallelRunner = ParallelCoroutineRunner()
            parallelRunner.run(listOfNotNull(
                if (locationSubsystem.locationAge.toMinutes() > 30) {
                    suspend {
                        sensorSubsystem.getLocation(SensorRefreshPolicy.Refresh)
                    }
                } else {
                    null
                },
                if (locationSubsystem.elevationAge.toMinutes() > 30) {
                    suspend {
                        sensorSubsystem.getElevation(SensorRefreshPolicy.Refresh)
                    }
                } else {
                    null
                }
            ))
        }
    }

    companion object {
        private const val UNIQUE_ID = 267389

        fun start(context: Context) {
            scheduler(context).interval(Duration.ofMinutes(30))
        }

        fun stop(context: Context) {
            scheduler(context).cancel()
        }

        private fun scheduler(context: Context): IPeriodicTaskScheduler {
            return PeriodicTaskSchedulerFactory(context).deferrable(
                WidgetUpdateWorker::class.java,
                UNIQUE_ID
            )
        }
    }
}