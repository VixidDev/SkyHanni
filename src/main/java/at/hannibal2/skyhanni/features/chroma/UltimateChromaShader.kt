package at.hannibal2.skyhanni.features.chroma

import at.hannibal2.skyhanni.api.minecraftevents.ClientEvents
import at.hannibal2.skyhanni.config.features.chroma.ChromaConfig.Direction
import at.hannibal2.skyhanni.mixins.transformers.AccessorMinecraft
import at.hannibal2.skyhanni.utils.compat.GuiScreenUtils
import at.hannibal2.skyhanni.utils.shader.Uniform
import net.minecraft.client.Minecraft

abstract class UltimateChromaShader(vertex: String, fragment: String) : ChromaShader(vertex, fragment) {

    var hues: FloatArray = floatArrayOf(305f / 360f, 290f / 360f, 255f / 360f)
    var saturations: FloatArray = floatArrayOf(1f, 1f, 1f)
    var brightnesses: FloatArray = floatArrayOf(1f, 0.5f, 1f)

    override fun registerUniforms() {
        registerUniform(Uniform.UniformType.FLOAT, "chromaSize") {
            ChromaManager.config.ultimateChromaSize.get() * (GuiScreenUtils.displayWidth / 100f)
        }
        registerUniform(Uniform.UniformType.FLOAT, "timeOffset") {
            //#if MC < 1.21
            var ticks = (ClientEvents.totalTicks) + (Minecraft.getMinecraft() as AccessorMinecraft).timer.renderPartialTicks
            //#else
            //$$ var ticks = (ClientEvents.totalTicks) + (MinecraftClient.getInstance() as AccessorMinecraft).timer.getTickProgress(true)
            //#endif

            ticks = when (ChromaManager.config.chromaDirection) {
                Direction.FORWARD_RIGHT, Direction.BACKWARD_RIGHT -> ticks
                Direction.FORWARD_LEFT, Direction.BACKWARD_LEFT -> -ticks
            }

            val chromaSpeed = ChromaManager.config.chromaSpeed / 360f
            ticks * chromaSpeed
        }
        registerUniform(Uniform.UniformType.FLOAT, "saturation") {
            ChromaManager.config.ultimateChromaSaturation
        }
        registerUniform(Uniform.UniformType.BOOL, "forwardDirection") {
            when (ChromaManager.config.chromaDirection) {
                Direction.FORWARD_RIGHT, Direction.FORWARD_LEFT -> true
                Direction.BACKWARD_RIGHT, Direction.BACKWARD_LEFT -> false
            }
        }
        registerUniform(Uniform.UniformType.VEC3, "hues") { hues }
        registerUniform(Uniform.UniformType.VEC3, "saturations") { saturations }
        registerUniform(Uniform.UniformType.VEC3, "brightnesses") { brightnesses }
    }

}
