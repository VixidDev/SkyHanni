package at.hannibal2.skyhanni.utils.renderables.interactables

import at.hannibal2.skyhanni.utils.GuiRenderUtils
import at.hannibal2.skyhanni.utils.KeyboardManager.isKeyClicked
import at.hannibal2.skyhanni.utils.RenderUtils.HorizontalAlignment
import at.hannibal2.skyhanni.utils.RenderUtils.VerticalAlignment
import at.hannibal2.skyhanni.utils.StringUtils.width
import at.hannibal2.skyhanni.utils.compat.GuiScreenUtils
import at.hannibal2.skyhanni.utils.compat.MouseCompat
import at.hannibal2.skyhanni.utils.renderables.Renderable
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import io.github.notenoughupdates.moulconfig.gui.MouseEvent
import io.github.notenoughupdates.moulconfig.gui.elements.GuiElementSlider
import io.github.notenoughupdates.moulconfig.gui.elements.GuiElementTextField
import io.github.notenoughupdates.moulconfig.observer.GetSetter
import io.github.notenoughupdates.moulconfig.observer.Property
import java.lang.reflect.Field
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class SliderRenderable(
    val text: String,
    val value: GetSetter<Float>,
    val minValue: Float,
    val maxValue: Float,
    val minStep: Float,
    override val horizontalAlign: HorizontalAlignment = HorizontalAlignment.LEFT,
    override val verticalAlign: VerticalAlignment = VerticalAlignment.TOP,
): Renderable {

    override val height: Int
    override val width: Int

    private var slider: GuiElementSlider
    private var textField: GuiElementTextField

    private var lastFrameX: Int = -1
    private var lastFrameY: Int = -1

    init {
        textField = GuiElementTextField(
            getStringifiedFloatValue(),
            GuiElementTextField.NO_SPACE or GuiElementTextField.NUM_ONLY or GuiElementTextField.SCALE_TEXT,
        )

        slider = GuiElementSlider(5, 15, 55, minValue, maxValue, minStep, value.get()) {
            value.set(it)
            textField.text = getStringifiedFloatValue()
        }

        width = slider.x + slider.width + 5 + (75 / 4) + 10
        height = slider.y + 20
    }

    /**
     * Taken from [MoulConfig][io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorSliderL.getStringifiedFloatValue]
     */
    private fun getStringifiedFloatValue(): String {
        val floatVal = value.get()
        var string: String
        if ((floatVal % 1).toInt() == 0) {
            string = floatVal.toInt().toString()
        } else {
            string = floatVal.toString()
            string = string.replace("(\\.\\d\\d\\d)\\d+".toRegex(), "$1")
            string = string.replace("0+$".toRegex(), "")
        }
        return string
    }

    override fun render(mouseOffsetX: Int, mouseOffsetY: Int) {
        GuiRenderUtils.drawStringCenteredScaledMaxWidth(text, width / 2f, 5f, true, text.width(), 0xc0c0c0)

        if (isHovered(mouseOffsetX, mouseOffsetY)) handleMouseClick(mouseOffsetX, mouseOffsetY, mouseMoved())
        handleKeyboardInput()

        val textFieldWidth = 75 / 4

        if (!Mouse.isButtonDown(0)) slider.setValue(value.get())

        slider.render()

        if (textField.focus) {
            textField.setOptions(GuiElementTextField.NO_SPACE or GuiElementTextField.NUM_ONLY)
            textField.setSize(textField.text.width() + 10, 16)
        } else {
            textField.text = getStringifiedFloatValue()
            textField.setSize(textFieldWidth, 16)
            textField.setOptions(GuiElementTextField.NO_SPACE or GuiElementTextField.NUM_ONLY or GuiElementTextField.SCALE_TEXT)
        }

        textField.render(slider.x + slider.width + 5, slider.y)
    }

    private fun handleMouseClick(mouseOffsetX: Int, mouseOffsetY: Int, fromMouseMoved: Boolean): Boolean {
        // Needs to be called on every down click, (up click?) and mouse move
        if (!((-99).isKeyClicked() or (-100).isKeyClicked()) && !fromMouseMoved) return false

        val pair = Renderable.currentRenderPassMousePosition
        val mouseX = pair?.let { it.first - mouseOffsetX } ?: 0
        val mouseY = pair?.let { it.second - mouseOffsetY } ?: 0

        val dummyClickEvent = MouseEvent.Click(0, false)

        if (slider.mouseInput(mouseX, mouseY, dummyClickEvent)) {
            textField.unfocus()
            return true
        }

        val textFieldWidth = 75 / 4
        val textFieldX = slider.x + slider.width + 5
        val textFieldY = slider.y
        textField.setSize(textFieldWidth, 16)

        if (mouseX > textFieldX && mouseX < textFieldX + textFieldWidth &&
            mouseY > textFieldY && mouseY < textFieldY + 16) {
            textField.mouseClicked(mouseX, mouseY, MouseCompat.getEventButton())
            return true
        }

        textField.unfocus()
        return false
    }

    private fun handleKeyboardInput(): Boolean {
        if (Keyboard.getEventKeyState() && textField.focus) {
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                textField.unfocus()
                return true
            }

            textField.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey())

            try {
                textField.setCustomBorderColour(0xFFFFFFFF.toInt())
                val f = textField.text.toFloat()
                value.set(f)
                slider.setValue(f)
            } catch (e: NumberFormatException) {
                textField.setCustomBorderColour(0xFFFF0000.toInt())
            }

            return true
        }
        return false
    }

    private fun mouseMoved(): Boolean {
        val mouseX = MouseCompat.getX()
        val mouseY = MouseCompat.getY()

        if (mouseX != lastFrameX || mouseY != lastFrameY) {
            lastFrameX = mouseX
            lastFrameY = mouseY
            return true
        }

        return false
    }

    companion object {
        fun Renderable.Companion.slider(
            text: String,
            value: GetSetter<Float>,
            minValue: Float,
            maxValue: Float,
            minStep: Float,
            horizontalAlign: HorizontalAlignment = HorizontalAlignment.LEFT,
            verticalAlign: VerticalAlignment = VerticalAlignment.TOP,
        ) = SliderRenderable(text, value, minValue, maxValue, minStep, horizontalAlign, verticalAlign)

        fun Renderable.Companion.slider(
            field: Field,
            instance: Any,
            horizontalAlign: HorizontalAlignment = HorizontalAlignment.LEFT,
            verticalAlign: VerticalAlignment = VerticalAlignment.TOP,
        ): SliderRenderable {
            field.isAccessible = true
            val property = field.get(instance) as Property<Float>
            val optionAnnotation = field.getAnnotation(ConfigOption::class.java)
            val optionName = optionAnnotation.name
            val editorAnnotation = field.getAnnotation(ConfigEditorSlider::class.java)
            val minValue = editorAnnotation.minValue
            val maxValue = editorAnnotation.maxValue
            val minStep = editorAnnotation.minStep
            return SliderRenderable(optionName, property, minValue, maxValue, minStep, horizontalAlign, verticalAlign)
        }
    }
}
