package com.kylecorry.trail_sense.shared.canvas.tiles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.util.Size
import com.kylecorry.andromeda.core.system.Screen
import com.kylecorry.andromeda.pdf.PDFRenderer2
import com.kylecorry.luna.coroutines.CoroutineQueueRunner
import com.kylecorry.luna.coroutines.onDefault
import com.kylecorry.luna.coroutines.onIO
import com.kylecorry.luna.coroutines.onMain
import com.kylecorry.sol.math.SolMath.roundNearest
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class PDFTileLoader(
    private val context: Context,
    private val uri: Uri,
    private val scaleStep: Float = 0.01f
) :
    TileLoader {

    override val tiles: List<Pair<ImageTile, Bitmap>>
        get() = listOfNotNull(baseImage) + tileMap.toList()

    private var baseImage: Pair<ImageTile, Bitmap>? = null
    private val tileMap = ConcurrentHashMap<ImageTile, Bitmap>()
    private val tileUpdateQueue = CoroutineQueueRunner()
    private var imageSize = Size(0, 0)
    private var renderer: PDFRenderer2? = null
    private var onTilesChangedListener: (() -> Unit)? = null

    override suspend fun updateTiles(zoom: Float, clipBounds: RectF) {
        onDefault {
            tileUpdateQueue.enqueue {
                onIO {
                    println("RENDERING")

                    if (renderer == null) {
                        renderer = PDFRenderer2(context, uri)
                    }

                    if (imageSize.width == 0 || imageSize.height == 0) {
                        val sizeNative = renderer!!.getSize()
                        imageSize = Size(
                            (sizeNative.width * Screen.dpi(context) / 72f).toInt(),
                            (sizeNative.height * Screen.dpi(context) / 72f).toInt()
                        )
                    }

                    if (baseImage == null) {
                        baseImage = ImageTile(0, 0, imageSize.width, imageSize.height) to
                                renderTile(
                                    ImageTile(0, 0, imageSize.width, imageSize.height),
                                    256
                                )!!
                    }

                    val tileSize = TileCreator.getTileSize(
                        imageSize.width,
                        imageSize.height,
                        zoom.roundNearest(scaleStep),
                        minimumSize = 128
                    ).width
                    val allTiles =
                        TileCreator.createTiles(
                            imageSize.width,
                            imageSize.height,
                            tileSize
                        )

                    // TODO: Why is the size off by 1?
                    val tiles = TileCreator.clip(
                        allTiles,
                        clipBounds,
                    ).map {
                        it.copy(width = it.width + 1, height = it.height + 1)
                    }

                    val adjustedBaseImageTile = baseImage?.first?.copy(
                        width = baseImage!!.first.width + 1,
                        height = baseImage!!.first.height + 1
                    )
                    val tilesToLoad =
                        tiles.filter { !tileMap.containsKey(it) && it != adjustedBaseImageTile }
                    val tilesToRemove = tileMap.keys.filter { !tiles.contains(it) }

//                val loadedTiles = ConcurrentHashMap<ImageTile, Bitmap>()

                    // TODO: Replace tile tree when loaded
                    val jobs = tilesToLoad.map { tile ->
                        launch {
                            val rendered = renderTile(tile, 256)!!
                            // Remove all other tiles completely contained by this tile
                            val toRemove = tiles.filter {
                                if (it == tile) return@filter false
                                val isContained = it.x >= tile.x && it.y >= tile.y &&
                                        it.x + it.width <= tile.x + tile.width &&
                                        it.y + it.height <= tile.y + tile.height
                                isContained
                            }
                            tileMap[tile] = rendered
                            toRemove.forEach {
                                tileMap[it]?.recycle()
                                tileMap.remove(it)
                            }
                        }
                    }

                    // Wait for all jobs to finish
                    jobs.joinAll()

//                tileMap.putAll(loadedTiles)
                    tilesToRemove.forEach {
                        tileMap[it]?.recycle()
                        tileMap.remove(it)
                    }
//                println("Tile size: $tileSize")
//                println("Tile count: ${tiles.size} / ${allTiles.size}")
//                println("Cache size: ${tileMap.size}")
//                println("Added: ${tilesToLoad.size}, Removed: ${tilesToRemove.size}")
//                println()
                }

                onMain {
                    onTilesChangedListener?.invoke()
                }
            }
        }
    }

    private fun renderTile(tile: ImageTile, destinationSize: Int): Bitmap? {
        return renderer?.toBitmap(
            Size(destinationSize, destinationSize),
            srcRegion = RectF(
                tile.x.toFloat(),
                tile.y.toFloat(),
                (tile.x + tile.width).toFloat(),
                (tile.y + tile.height).toFloat()
            )
        )
    }

    override fun release() {
        tileUpdateQueue.cancel()
        tileMap.values.forEach {
            it.recycle()
        }
        baseImage?.second?.recycle()
        baseImage = null
        tileMap.clear()
        renderer = null
    }

    override fun setOnTilesChangedListener(listener: () -> Unit) {
        onTilesChangedListener = listener
    }
}