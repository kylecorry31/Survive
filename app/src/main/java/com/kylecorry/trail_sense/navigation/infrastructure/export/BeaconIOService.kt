package com.kylecorry.trail_sense.navigation.infrastructure.export

import android.content.Context
import com.kylecorry.andromeda.core.time.toZonedDateTime
import com.kylecorry.andromeda.gpx.GPXData
import com.kylecorry.andromeda.gpx.GPXParser
import com.kylecorry.andromeda.gpx.GPXWaypoint
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.navigation.domain.BeaconEntity
import com.kylecorry.trail_sense.navigation.domain.BeaconGroupEntity
import com.kylecorry.trail_sense.navigation.infrastructure.persistence.BeaconRepo
import com.kylecorry.trail_sense.shared.AppColor
import com.kylecorry.trail_sense.shared.FormatService
import com.kylecorry.trailsensecore.domain.navigation.Beacon
import com.kylecorry.trailsensecore.domain.navigation.BeaconGroup

class BeaconIOService(private val context: Context) {

    private val repo by lazy { BeaconRepo.getInstance(context) }
    private val formatService by lazy { FormatService(context) }

    fun export(waypoints: List<GPXWaypoint>): String {
        return GPXParser.toGPX(GPXData(waypoints, listOf()), context.getString(R.string.app_name))
    }

    fun getGPXWaypoints(beacons: List<Beacon>, groups: List<BeaconGroup>): List<GPXWaypoint> {
        val groupNames = mutableMapOf<Long, String>()
        for (group in groups) {
            groupNames[group.id] = group.name
        }

        return beacons.map {
            GPXWaypoint(
                it.coordinate,
                it.name,
                it.elevation,
                it.comment,
                null,
                if (it.beaconGroupId == null) null else groupNames[it.beaconGroupId]
            )
        }
    }

    fun getGPXWaypoints(gpx: String): List<GPXWaypoint> {
        return GPXParser.parse(gpx).waypoints
    }

    suspend fun import(waypoints: List<GPXWaypoint>): Int {
        val groupNames = waypoints.mapNotNull { it.group }.distinct()

        val groupIdMap = mutableMapOf<String, Long>()
        groupNames.forEach {
            val id = repo.addBeaconGroup(BeaconGroupEntity(it).also { it.id = 0 })
            groupIdMap[it] = id
        }

        val beacons = waypoints.map {
            val name = it.name ?:
                (if (it.time != null) formatService.formatDateTime(it.time!!.toZonedDateTime()) else null) ?:
                formatService.formatLocation(it.coordinate)
            Beacon(
                0,
                name,
                it.coordinate,
                comment = it.comment,
                elevation = it.elevation,
                beaconGroupId = if (it.group != null) groupIdMap[it.group] else null,
                color = AppColor.Orange.color
            )
        }

        beacons.forEach {
            repo.addBeacon(BeaconEntity.from(it))
        }

        return waypoints.size
    }

}