package at.hannibal2.skyhanni.features.chroma

/**
 * A textured chroma type explicitly for Ultimate enchants. Could be used with other textured
 * GUI elements if one wished.
 *
 * See [TexturedChromaShader][at.hannibal2.skyhanni.features.chroma.TexturedChromaShader] for usage on textured chroma shaders.
 */
object UltimateTexturedChromaShader : UltimateChromaShader("ultimate_textured_chroma", "ultimate_textured_chroma") {
    val INSTANCE: UltimateTexturedChromaShader
        get() = this
}
