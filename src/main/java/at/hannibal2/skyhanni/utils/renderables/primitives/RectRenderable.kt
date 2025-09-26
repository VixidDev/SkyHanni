package at.hannibal2.skyhanni.utils.renderables.primitives

import at.hannibal2.skyhanni.features.chroma.ChromaShaderManager
import at.hannibal2.skyhanni.features.chroma.ChromaType
import at.hannibal2.skyhanni.utils.GuiRenderUtils
import at.hannibal2.skyhanni.utils.RenderUtils.HorizontalAlignment
import at.hannibal2.skyhanni.utils.RenderUtils.VerticalAlignment
import at.hannibal2.skyhanni.utils.renderables.Renderable
import java.awt.Color

open class RectRenderable protected constructor(
    override val width: Int,
    override val height: Int,
    private val color: Color,
    private val chromaType: ChromaType?,
    override val horizontalAlign: HorizontalAlignment = HorizontalAlignment.LEFT,
    override val verticalAlign: VerticalAlignment = VerticalAlignment.TOP,
): Renderable {

    override fun render(mouseOffsetX: Int, mouseOffsetY: Int) {
        chromaType?.let { ChromaShaderManager.begin(it) }
        GuiRenderUtils.drawRect(0, 0, width, height, color.rgb)
        chromaType?.let { ChromaShaderManager.end() }
    }

    companion object {
        fun Renderable.Companion.rect(
            width: Int,
            height: Int,
            color: Color,
            chromaType: ChromaType? = null,
            horizontalAlign: HorizontalAlignment = HorizontalAlignment.LEFT,
            verticalAlign: VerticalAlignment = VerticalAlignment.TOP
        ) = RectRenderable(width, height, color, chromaType, horizontalAlign, verticalAlign)
    }
}
