package at.hannibal2.skyhanni.features.chroma

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.utils.ColorUtils
import at.hannibal2.skyhanni.utils.shader.ShaderHelper
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11

/**
 * Class to handle chroma font rendering
 *
 * Modified class from SkyblockAddons
 *
 * Credit: [DrawStateFontRenderer.java](https://github.com/BiscuitDevelopment/SkyblockAddons/blob/main/src/main/java/codes/biscuit/skyblockaddons/utils/draw/DrawStateFontRenderer.java)
 */
class ChromaFontRenderer(private val baseColor: Int) {

    private var chromaOn = false

    fun startChroma() {
        chromaOn = true
    }

    fun endChroma() {
        chromaOn = false
    }

    fun loadChromaEnv(formatIndex: Int = 22) {
        if (chromaOn) {
            newChromaEnv(formatIndex)
        }
    }

    fun restoreChromaEnv() {
        if (ShaderHelper.areShadersSupported() && !chromaOn) ChromaShaderManager.end()
    }

    fun newChromaEnv(formatIndex: Int): ChromaFontRenderer {
        if (ShaderHelper.areShadersSupported()) {

            val chromaUltimate = SkyHanniMod.feature.inventory.enchantParsing.chromaUltimates.get()

            val chromaType = when (formatIndex) {
                23 -> {
                    if (chromaUltimate) ChromaType.ULTIMATE_TEXTURED else ChromaType.TEXTURED
                }
                else -> ChromaType.TEXTURED
            }

            ChromaShaderManager.begin(chromaType)
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
        }
        return this
    }

    fun bindActualColor(alpha: Float): ChromaFontRenderer {
        GlStateManager.color(
            ColorUtils.getRed(baseColor).toFloat() / 255f,
            ColorUtils.getGreen(baseColor).toFloat() / 255f,
            ColorUtils.getBlue(baseColor).toFloat() / 255f,
            alpha
        )
        return this
    }

    fun endChromaEnv(): ChromaFontRenderer {
        if (ShaderHelper.areShadersSupported()) {
            ChromaShaderManager.end()
            GlStateManager.shadeModel(GL11.GL_FLAT)
        }
        return this
    }

    fun getChromaState() = chromaOn
}
