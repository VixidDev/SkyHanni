package at.hannibal2.skyhanni.test.renderable

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.config.commands.CommandCategory
import at.hannibal2.skyhanni.config.commands.CommandRegistrationEvent
import at.hannibal2.skyhanni.config.commands.brigadier.BrigadierArguments
import at.hannibal2.skyhanni.config.commands.brigadier.arguments.InternalNameArgumentType
import at.hannibal2.skyhanni.features.chroma.ChromaType
import at.hannibal2.skyhanni.features.chroma.UltimateChromaShader
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.LorenzColor
import at.hannibal2.skyhanni.utils.renderables.Renderable
import at.hannibal2.skyhanni.utils.renderables.container.HorizontalContainerRenderable.Companion.horizontal
import at.hannibal2.skyhanni.utils.renderables.container.VerticalContainerRenderable.Companion.vertical
import at.hannibal2.skyhanni.utils.renderables.primitives.RectRenderable.Companion.rect
import at.hannibal2.skyhanni.utils.renderables.primitives.text
import at.hannibal2.skyhanni.utils.shader.ShaderManager

@SkyHanniModule(devOnly = true)
object TestChromaGradient : RenderableTestSuite.TestRenderable("chroma_gradient") {

    override fun renderable(): Renderable {
        return with(Renderable) {
            horizontal(
                vertical(
                    text("§ZStandard Chroma"),
                    rect(200, 100, LorenzColor.WHITE.toColor(), ChromaType.STANDARD)
                ),
                vertical(
                    text("§XUltimate Chroma"),
                    rect(200, 100, LorenzColor.WHITE.toColor(), ChromaType.ULTIMATE_STANDARD)
                ),
                spacing = 2
            )
        }
    }

    @HandleEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.registerBrigadier("shultimatechroma") {
            category = CommandCategory.DEVELOPER_TEST
            description = "Used for changing ultimate chroma hues and brightnesses."
            arg("hue1", BrigadierArguments.float()) { hue1 ->
                arg("saturation1", BrigadierArguments.float()) { saturation1 ->
                    arg("brightness1", BrigadierArguments.float()) { brightness1 ->
                        arg("hue2", BrigadierArguments.float()) { hue2 ->
                            arg("saturation2", BrigadierArguments.float()) { saturation2 ->
                                arg("brightness2", BrigadierArguments.float()) { brightness2 ->
                                    arg("hue3", BrigadierArguments.float()) { hue3 ->
                                        arg("saturation3", BrigadierArguments.float()) { saturation3 ->
                                            arg("brightness3", BrigadierArguments.float()) { brightness3 ->
                                                callback {
                                                    (ShaderManager.Shaders.ULTIMATE_STANDARD.shader as UltimateChromaShader).hues =
                                                        floatArrayOf(getArg(hue1), getArg(hue2), getArg(hue3))
                                                    ShaderManager.Shaders.ULTIMATE_STANDARD.shader.saturations =
                                                        floatArrayOf(getArg(saturation1), getArg(saturation2), getArg(saturation3))
                                                    ShaderManager.Shaders.ULTIMATE_STANDARD.shader.brightnesses =
                                                        floatArrayOf(getArg(brightness1), getArg(brightness2), getArg(brightness3))
                                                    (ShaderManager.Shaders.ULTIMATE_CHROMA.shader as UltimateChromaShader).hues =
                                                        floatArrayOf(getArg(hue1), getArg(hue2), getArg(hue3))
                                                    ShaderManager.Shaders.ULTIMATE_CHROMA.shader.saturations =
                                                        floatArrayOf(getArg(saturation1), getArg(saturation2), getArg(saturation3))
                                                    ShaderManager.Shaders.ULTIMATE_CHROMA.shader.brightnesses =
                                                        floatArrayOf(getArg(brightness1), getArg(brightness2), getArg(brightness3))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
