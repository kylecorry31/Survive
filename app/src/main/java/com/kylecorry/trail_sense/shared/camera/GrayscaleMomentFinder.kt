package com.kylecorry.trail_sense.shared.camera

import android.graphics.Bitmap
import com.kylecorry.andromeda.core.units.PixelCoordinate
import com.kylecorry.sol.math.SolMath.positive
import com.kylecorry.trail_sense.shared.colors.ColorUtils
import kotlin.math.abs

private val fl = 0.2f

class GrayscaleMomentFinder(
    private val threshold: Int,
    private val minPixels: Int,
    private val desiredAspect: Float? = null,
    private val aspectThreshold: Float = 0.2f
) {

    fun getMoment(bitmap: Bitmap): PixelCoordinate? {
        var momentX = 0f
        var momentY = 0f
        var total = 0f
        var count = 0
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(x, y)
                val brightness = ColorUtils.average(pixel)
                if (brightness >= threshold) {
                    momentX += x * brightness
                    momentY += y * brightness
                    total += brightness
                    count++
                }
            }
        }

        if (count < minPixels || total == 0f) {
            return null
        }

        val x = momentX / total
        val y = momentY / total

        if (desiredAspect == null) {
            return PixelCoordinate(x, y)
        }

        var minXPos = 0
        var minYPos = 0
        var maxXPos = 0
        var maxYPos = 0

        // Search on either side of the x and y to find the bounds
        var currX = x.toInt()
        while (currX >= 0 && ColorUtils.average(bitmap.getPixel(currX, y.toInt())) >= threshold) {
            minXPos = currX
            currX--
        }

        currX = x.toInt()
        while (currX < bitmap.width && ColorUtils.average(
                bitmap.getPixel(
                    currX,
                    y.toInt()
                )
            ) >= threshold
        ) {
            maxXPos = currX
            currX++
        }

        var currY = y.toInt()
        while (currY >= 0 && ColorUtils.average(bitmap.getPixel(x.toInt(), currY)) >= threshold) {
            minYPos = currY
            currY--
        }

        currY = y.toInt()
        while (currY < bitmap.height && ColorUtils.average(
                bitmap.getPixel(
                    x.toInt(),
                    currY
                )
            ) >= threshold
        ) {
            maxYPos = currY
            currY++
        }

        val width = maxXPos - minXPos
        val height = maxYPos - minYPos

        val aspect = width / height.toFloat().positive(1f)
        if (abs(aspect - desiredAspect) > aspectThreshold) {
            return null
        }

        return PixelCoordinate(x, y)
    }

}