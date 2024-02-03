package com.kylecorry.trail_sense.tools.augmented_reality.ui.layers

import android.graphics.Color
import com.kylecorry.andromeda.canvas.ICanvasDrawer
import com.kylecorry.andromeda.core.ui.Colors.withAlpha
import com.kylecorry.andromeda.core.units.PixelCoordinate
import com.kylecorry.sol.math.SolMath
import com.kylecorry.sol.math.SolMath.toDegrees
import com.kylecorry.sol.math.analysis.Trigonometry
import com.kylecorry.sol.math.geometry.Rectangle
import com.kylecorry.sol.units.Bearing
import com.kylecorry.sol.units.Coordinate
import com.kylecorry.sol.units.Distance
import com.kylecorry.trail_sense.shared.canvas.LineClipper
import com.kylecorry.trail_sense.shared.canvas.LineInterpolator
import com.kylecorry.trail_sense.shared.extensions.squaredDistanceTo
import com.kylecorry.trail_sense.tools.augmented_reality.domain.position.ARPoint
import com.kylecorry.trail_sense.tools.augmented_reality.domain.position.GeographicARPoint
import com.kylecorry.trail_sense.tools.augmented_reality.ui.ARMarker
import com.kylecorry.trail_sense.tools.augmented_reality.ui.AugmentedRealityView
import com.kylecorry.trail_sense.tools.augmented_reality.ui.CanvasCircle
import com.kylecorry.trail_sense.tools.navigation.domain.NavigationService
import com.kylecorry.trail_sense.tools.navigation.ui.IMappablePath
import com.kylecorry.trail_sense.tools.paths.ui.IPathLayer
import kotlin.math.atan2
import kotlin.math.sqrt

class ARPathLayer(viewDistance: Distance) : ARLayer, IPathLayer {

    private val pointLayer = ARMarkerLayer(0.2f, 24f)
    private var lastLocation = Coordinate.zero
    private var lastElevation: Float? = null
    private val viewDistanceMeters = viewDistance.meters().distance
    private val center = PixelCoordinate(viewDistanceMeters, viewDistanceMeters)
    private val bounds = Rectangle(
        0f,
        viewDistanceMeters * 2,
        viewDistanceMeters * 2,
        0f,
    )
    private val clipper = LineClipper()
    private val navigation = NavigationService()
    private val interpolator = LineInterpolator()

    // The distance at which the elevation of the closest point is used to adjust the elevation of all points
    private val elevationOverrideDistance = 30f // meters
    private val squareElevationOverrideDistance = SolMath.square(elevationOverrideDistance)

    private val pointSpacing = 4f // meters
    private val pathSimplification = 1f // meters (high quality)
    private val pointSize = 0.75f // meters

    // A limit to ensure performance is not impacted
    private val nearbyLimit = ((viewDistanceMeters / pointSpacing).toInt()).coerceIn(20, 60)

    override fun draw(drawer: ICanvasDrawer, view: AugmentedRealityView) {
        lastLocation = view.location
        lastElevation = view.altitude
        pointLayer.draw(drawer, view)
    }

    override fun invalidate() {
        pointLayer.invalidate()
    }

    override fun onClick(
        drawer: ICanvasDrawer,
        view: AugmentedRealityView,
        pixel: PixelCoordinate
    ): Boolean {
        return false
    }

    override fun onFocus(drawer: ICanvasDrawer, view: AugmentedRealityView): Boolean {
        return false
    }

    override fun setPaths(paths: List<IMappablePath>) {
        val location = lastLocation
        val elevation = lastElevation

        // TODO: Render the other nearby paths at a lower opacity and number of points
        val ps = paths.map {
            val circle = CanvasCircle(it.color, Color.WHITE, 175)
            // These are sorted by farthest to closest
            val nearby = getNearbyARPoints(it, location, elevation)
            nearby.map {
                ARMarker(it, circle)
            } to nearby.lastOrNull()?.location?.distanceTo(location)
        }

        val markers =
            ps.minByOrNull { it.second ?: Float.POSITIVE_INFINITY }?.first ?: emptyList()

        // TODO: Allow the user to choose the number of paths to display
        // All
//        val markers = ps.flatMap { it.first }

        pointLayer.setMarkers(markers)
    }

    private fun getNearbyARPoints(
        path: IMappablePath,
        location: Coordinate,
        elevation: Float?
    ): List<GeographicARPoint> {
        // It is easier to work with the points if they are projected onto a cartesian plane

        // Step 1: Project the path points
        val projected = path.points.map {
            project(it.coordinate, location)
        }
        val fallbackElevation = path.points.firstOrNull { it.elevation != null }?.elevation
        val hasElevation = fallbackElevation != null
        val z =
            if (hasElevation) path.points.map { it.elevation ?: fallbackElevation ?: 0f } else null

        // Step 2: Clip the projected points
        val output = mutableListOf<Float>()
        val zOutput = if (hasElevation) mutableListOf<Float>() else null
        clipper.clip(
            projected,
            bounds,
            output,
            zValues = z,
            zOutput = zOutput,
//            rdpFilterEpsilon = pathSimplification
        )

        // Step 3: Interpolate between the points for a higher resolution
        val output2 = mutableListOf<Float>()
        val zOutput2 = if (hasElevation) mutableListOf<Float>() else null
        interpolator.increaseResolution(
            output,
            output2,
            pointSpacing,
            z = zOutput,
            zOutput = zOutput2
        )

        // Step 4: Cleanup and sorting
        val points = mutableListOf<Pair<PixelCoordinate, Float?>>()
        for (i in output2.indices step 2) {
            val x = output2[i]
            val y = output2[i + 1]
            val pointZ = zOutput2?.getOrNull(i / 2)
            points.add(PixelCoordinate(x, y) to pointZ)
        }

        // Remove duplicate points and take top n closest points
        val nearby = points
            .asSequence()
            .distinctBy { it.first }
            .sortedBy { it.first.squaredDistanceTo(center) }
            .take(nearbyLimit)
            .toList()
            .reversed()

        // Step 5: Adjust the elevation of the points
        val closest = nearby.lastOrNull()
        val closestSquareDist = closest?.first?.squaredDistanceTo(center)

        // If the closest point is within the elevation override distance, override the elevation of all points
        val elevationOffset =
            if (hasElevation && elevation != null && closestSquareDist != null && closestSquareDist < squareElevationOverrideDistance) {
                closest.second?.minus(elevation) ?: 0f
            } else {
                null
            }

        // Step 6: Convert the points to AR points
        return nearby.map {
            GeographicARPoint(
                inverseProject(it.first, location),
                it.second?.minus(elevationOffset ?: 0f),
                pointSize
            )
        }
    }

    // TODO: Extract this to sol (azimuthal equidistant projection)
    private fun project(
        location: Coordinate,
        myLocation: Coordinate
    ): PixelCoordinate {
        val vector = navigation.navigate(myLocation, location, 0f, true)
        val angle = Trigonometry.toUnitAngle(vector.direction.value, 90f, false)
        val pixelDistance = vector.distance // Assumes 1 meter = 1 pixel
        val xDiff = SolMath.cosDegrees(angle) * pixelDistance
        val yDiff = SolMath.sinDegrees(angle) * pixelDistance
        return PixelCoordinate(center.x + xDiff, center.y - yDiff)
    }

    private fun inverseProject(
        pixel: PixelCoordinate,
        myLocation: Coordinate
    ): Coordinate {
        val xDiff = pixel.x - center.x
        val yDiff = center.y - pixel.y
        val pixelDistance = sqrt(xDiff * xDiff + yDiff * yDiff)
        val angle = atan2(yDiff, xDiff).toDegrees()
        val direction = Trigonometry.remapUnitAngle(angle, 90f, false)
        return myLocation.plus(pixelDistance.toDouble(), Bearing(direction))
    }
}