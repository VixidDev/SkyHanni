package at.hannibal2.skyhanni.features.misc

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.LorenzChatEvent
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.LorenzUtils.between
import at.hannibal2.skyhanni.utils.RenderUtils.renderString
import at.hannibal2.skyhanni.utils.StringUtils.matchRegex
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class CurrentPetDisplay {

    @SubscribeEvent
    fun onChatMessage(event: LorenzChatEvent) {
        if (!LorenzUtils.inSkyBlock) return

        var blocked = false

        val message = event.message
        if (message.matchRegex("§aYou summoned your §r(.*)§r§a!")) {
            SkyHanniMod.feature.hidden.currentPet = message.between("your §r", "§r§a")
            blocked = true
        }
        if (message.matchRegex("§cAutopet §eequipped your §7(.*)§e! §a§lVIEW RULE")) {
            SkyHanniMod.feature.hidden.currentPet = message.between("] ", "§e!")
            blocked = true
        }
        if (message.matchRegex("§aYou despawned your §r(.*)§r§a!")) {
            SkyHanniMod.feature.hidden.currentPet = ""
            blocked = true
        }

        if (blocked && SkyHanniMod.feature.misc.petDisplay) {
            event.blockedReason = "pets"
        }
    }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent.GameOverlayRenderEvent) {
        if (!LorenzUtils.inSkyBlock) return

        if (!SkyHanniMod.feature.misc.petDisplay) return

        SkyHanniMod.feature.misc.petDisplayPos.renderString(SkyHanniMod.feature.hidden.currentPet)
    }
}