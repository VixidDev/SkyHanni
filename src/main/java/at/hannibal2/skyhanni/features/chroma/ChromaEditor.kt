package at.hannibal2.skyhanni.features.chroma

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.config.core.config.Position
import at.hannibal2.skyhanni.utils.LorenzColor
import at.hannibal2.skyhanni.utils.RenderUtils
import at.hannibal2.skyhanni.utils.RenderUtils.renderRenderable
import at.hannibal2.skyhanni.utils.compat.SkyhanniBaseScreen
import at.hannibal2.skyhanni.utils.renderables.Renderable
import at.hannibal2.skyhanni.utils.renderables.container.HorizontalContainerRenderable.Companion.horizontal
import at.hannibal2.skyhanni.utils.renderables.container.VerticalContainerRenderable.Companion.vertical
import at.hannibal2.skyhanni.utils.renderables.interactables.SliderRenderable.Companion.slider
import at.hannibal2.skyhanni.utils.renderables.primitives.RectRenderable.Companion.rect
import at.hannibal2.skyhanni.utils.renderables.primitives.placeholder
import at.hannibal2.skyhanni.utils.renderables.primitives.text

class ChromaEditor : SkyhanniBaseScreen() {

    private val position: Position = Position().ignoreScale()

    private var showStandard = true
    private var recreateRenderable = false

    private var renderable = createRenderable()

    override fun onDrawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (recreateRenderable) {
            renderable = createRenderable()
            recreateRenderable = false
        }

        drawDefaultBackground(mouseX, mouseY, partialTicks)

        val left = (width - renderable.width) / 2
        val top = (height - renderable.height) / 2
        position.moveTo(left, top)

        position.renderRenderable(renderable, "Chroma Editor", false)
    }

    private fun createRenderable(): Renderable {
        val config = SkyHanniMod.feature.gui.chroma

        val standardColorFields = with(Renderable) {
            text("")
        }

        val ultimateColorFields = with(Renderable) {
            text("")
        }

        val standardEditor = with(Renderable) {
            vertical(
                placeholder(300, 0, horizontalAlign = RenderUtils.HorizontalAlignment.CENTER),
                vertical(
                    text("Preview", horizontalAlign = RenderUtils.HorizontalAlignment.CENTER),
                    rect(
                        200,
                        100,
                        LorenzColor.WHITE.toColor(),
                        ChromaType.STANDARD
                    ),
                    spacing = 2,
                    horizontalAlign = RenderUtils.HorizontalAlignment.CENTER
                ),
                placeholder(0, 2),
                horizontal(
                    vertical(
                        placeholder(140, 0, horizontalAlign = RenderUtils.HorizontalAlignment.CENTER),
                        clickable(
                            drawInsideDarkRect(
                                text("Reset to original chroma"),
                                horizontalAlign = RenderUtils.HorizontalAlignment.CENTER,
                            ),
                            onLeftClick = {}
                        ),
                        standardColorFields,
                        clickable(
                            drawInsideDarkRect(text("Add Color"),
                                horizontalAlign = RenderUtils.HorizontalAlignment.LEFT
                            ),
                            onLeftClick = {}
                        )
                    ),
                    vertical(
                        placeholder(140, 0, horizontalAlign = RenderUtils.HorizontalAlignment.CENTER),
                        slider(
                            config::class.java.getDeclaredField("chromaSize"),
                            config,
                            horizontalAlign = RenderUtils.HorizontalAlignment.CENTER,
                        ).renderBounds()
                    ),
                    spacing = 2,
                    horizontalAlign = RenderUtils.HorizontalAlignment.CENTER
                ),
                spacing = 2
            )
        }

        val ultimateEditor = with(Renderable) {
            vertical(
                text("Preview", horizontalAlign = RenderUtils.HorizontalAlignment.CENTER),
                rect(
                    200, 100,
                    LorenzColor.WHITE.toColor(),
                    ChromaType.ULTIMATE_STANDARD,
                    horizontalAlign = RenderUtils.HorizontalAlignment.CENTER
                ),
                horizontal(
                    vertical(
                        ultimateColorFields,
                        clickable(
                            drawInsideDarkRect(text("Add Color")),
                            onLeftClick = {}
                        )
                    ),
                    vertical(
                        slider(
                            config::class.java.getDeclaredField("ultimateChromaSize"),
                            config,
                            horizontalAlign = RenderUtils.HorizontalAlignment.CENTER,
                        ).renderBounds()
                    ),
                    spacing = 2
                ),
                spacing = 2
            )
        }

        val editorToShow = if (showStandard) standardEditor else ultimateEditor

        return with(Renderable) {
            drawInsideDarkRect(
                vertical(
                    placeholder(300, 2, horizontalAlign = RenderUtils.HorizontalAlignment.CENTER),
                    text("Chroma Editor", scale = 2.0, horizontalAlign = RenderUtils.HorizontalAlignment.CENTER),
                    placeholder(0, 2),
                    horizontal(
                        clickable(
                            drawInsideDarkRect(text("Standard Chroma")),
                            onLeftClick = {
                                showStandard = true
                                recreateRenderable = true
                            }
                        ),
                        clickable(
                            drawInsideDarkRect(text("Ultimate Chroma")),
                            onLeftClick = {
                                showStandard = false
                                recreateRenderable = true
                            }
                        ),
                        spacing = 2,
                        horizontalAlign = RenderUtils.HorizontalAlignment.CENTER
                    ),
                    placeholder(0, 2),
                    editorToShow,
                    placeholder(0, 2)
                )
            )
        }
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}
