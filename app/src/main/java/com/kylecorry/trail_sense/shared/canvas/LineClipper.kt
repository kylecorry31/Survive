package com.kylecorry.trail_sense.shared.canvas

import com.kylecorry.andromeda.core.units.PixelCoordinate
import com.kylecorry.sol.math.Vector2
import com.kylecorry.sol.math.filters.RDPFilter
import com.kylecorry.sol.math.geometry.Geometry
import com.kylecorry.sol.math.geometry.Line
import com.kylecorry.sol.math.geometry.Rectangle
import com.kylecorry.sol.science.geology.Geology
import com.kylecorry.trail_sense.navigation.paths.domain.PathPoint
import com.kylecorry.trail_sense.shared.extensions.isSamePixel
import com.kylecorry.trail_sense.shared.toPixelCoordinate
import com.kylecorry.trail_sense.shared.toVector2
import kotlin.math.absoluteValue

class LineClipper {

    fun clip(
        pixels: List<PixelCoordinate>,
        bounds: Rectangle,
        output: MutableList<Float>,
        origin: PixelCoordinate = PixelCoordinate(0f, 0f),
        preventLineWrapping: Boolean = false,
        rdpFilterEpsilon: Float? = null
    ) {
        if (isOutOfBounds(pixels, bounds)) {
            return
        }

        var previous: PixelCoordinate? = null

        val filter =
            if (rdpFilterEpsilon != null) RDPFilter<PixelCoordinate>(rdpFilterEpsilon) { point, start, end ->
                Geometry.pointLineDistance(
                    point.toVector2(bounds.top),
                    Line(start.toVector2(bounds.top), end.toVector2(bounds.top))
                ).absoluteValue
            } else null

        val filtered = filter?.filter(pixels) ?: pixels

        val multiplier = 1.5f

        val minX = bounds.width() * -multiplier
        val maxX = bounds.width() * (1 + multiplier)
        val minY = bounds.height() * -multiplier
        val maxY = bounds.height() * (1 + multiplier)

        for (pixel in filtered) {
            // Remove lines that cross the entire screen (because they are behind the camera)
            val isLineInvalid = preventLineWrapping && previous != null &&
                    (pixel.x < minX && previous.x > maxX ||
                            pixel.x > maxX && previous.x < minX ||
                            pixel.y < minY && previous.y > maxY ||
                            pixel.y > maxY && previous.y < minY)

            if (previous != null && !isLineInvalid) {
                // If the end point is the same as the previous, don't draw a line
                if (previous.isSamePixel(pixel)) {
                    continue
                }
                addLine(bounds, previous, pixel, origin, output)
            }
            previous = pixel
        }
    }

    private fun isOutOfBounds(pixels: List<PixelCoordinate>, bounds: Rectangle): Boolean {
        for (i in 1 until pixels.size) {
            val start = pixels[i - 1].toVector2(bounds.top)
            val end = pixels[i].toVector2(bounds.top)
            if (!(start.x < bounds.left && end.x < bounds.left ||
                        start.x > bounds.right && end.x > bounds.right ||
                        start.y < bounds.bottom && end.y < bounds.bottom ||
                        start.y > bounds.top && end.y > bounds.top)
            ) {
                // Potential intersection
                return false
            }
        }

        return true
    }

    private fun addLine(
        bounds: Rectangle,
        start: PixelCoordinate,
        end: PixelCoordinate,
        origin: PixelCoordinate,
        lines: MutableList<Float>
    ) {
        val a = start.toVector2(bounds.top)
        val b = end.toVector2(bounds.top)

        // Both are in
        if (bounds.contains(a) && bounds.contains(b)) {
            lines.add(start.x - origin.x)
            lines.add(start.y - origin.y)
            lines.add(end.x - origin.x)
            lines.add(end.y - origin.y)
            return
        }

        val intersection =
            Geometry.getIntersection(a, b, bounds).map { it.toPixelCoordinate(bounds.top) }

        // A is in, B is not
        if (bounds.contains(a)) {
            if (intersection.any()) {
                lines.add(start.x - origin.x)
                lines.add(start.y - origin.y)
                lines.add(intersection[0].x - origin.x)
                lines.add(intersection[0].y - origin.y)
            }
            return
        }

        // B is in, A is not
        if (bounds.contains(b)) {
            if (intersection.any()) {
                lines.add(intersection[0].x - origin.x)
                lines.add(intersection[0].y - origin.y)
                lines.add(end.x - origin.x)
                lines.add(end.y - origin.y)
            }
            return
        }

        // Both are out, but may intersect
        if (intersection.size == 2) {
            lines.add(intersection[0].x - origin.x)
            lines.add(intersection[0].y - origin.y)
            lines.add(intersection[1].x - origin.x)
            lines.add(intersection[1].y - origin.y)
        }
    }

}