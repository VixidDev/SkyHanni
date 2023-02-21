package at.hannibal2.skyhanni.features.nether.reputationhelper.dailyquest

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.GuiContainerEvent
import at.hannibal2.skyhanni.events.LorenzChatEvent
import at.hannibal2.skyhanni.events.ProfileApiDataLoadedEvent
import at.hannibal2.skyhanni.features.nether.reputationhelper.CrimsonIsleReputationHelper
import at.hannibal2.skyhanni.features.nether.reputationhelper.FactionType
import at.hannibal2.skyhanni.features.nether.reputationhelper.dailykuudra.KuudraTier
import at.hannibal2.skyhanni.features.nether.reputationhelper.dailyquest.quest.*
import at.hannibal2.skyhanni.features.nether.reputationhelper.miniboss.CrimsonMiniBoss
import at.hannibal2.skyhanni.test.GriffinUtils.drawWaypointFilled
import at.hannibal2.skyhanni.utils.*
import at.hannibal2.skyhanni.utils.InventoryUtils.getInventoryName
import at.hannibal2.skyhanni.utils.ItemUtils.getLore
import at.hannibal2.skyhanni.utils.ItemUtils.name
import at.hannibal2.skyhanni.utils.RenderUtils.drawDynamicText
import at.hannibal2.skyhanni.utils.RenderUtils.highlight
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*

class DailyQuestHelper(val reputationHelper: CrimsonIsleReputationHelper) {

    private val townBoardMage = LorenzVec(-138, 92, -754)
    private val townBoardBarbarian = LorenzVec(-572, 100, -687)

    private var tick = 0
    private val loader = QuestLoader(this)
    val quests = mutableListOf<Quest>()
    private val sacksCache = mutableMapOf<String, Long>()
    private var latestTrophyFishInInventory = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!LorenzUtils.inSkyBlock) return
        if (LorenzUtils.skyBlockIsland != IslandType.CRIMSON_ISLE) return
        if (!SkyHanniMod.feature.misc.crimsonIsleReputationHelper) return
        tick++
        if (tick % 20 == 0) {
            loader.checkInventory()
            checkInventoryForTrophyFish()
        }

        if (tick % 60 == 0) {
            checkInventoryForFetchItem()
            loader.loadFromTabList()

            if (quests.size > 5) {
                reputationHelper.reset()
            }
        }
    }

    private fun checkInventoryForTrophyFish() {
        val fishQuest = getQuest<TrophyFishQuest>() ?: return
        if (fishQuest.state != QuestState.ACCEPTED && fishQuest.state != QuestState.READY_TO_COLLECT) return

        val fishName = fishQuest.fishName
        val currentlyInInventory = InventoryUtils.countItemsInLowerInventory { it.name?.contains(fishName) ?: false }
        val diff = currentlyInInventory - latestTrophyFishInInventory
        if (diff < 1) return
        latestTrophyFishInInventory = currentlyInInventory
        updateProcessQuest(fishQuest, fishQuest.haveAmount + diff)
    }

    fun update() {
        reputationHelper.update()
    }

    @SubscribeEvent
    fun onBackgroundDrawn(event: GuiContainerEvent.BackgroundDrawnEvent) {
        if (!LorenzUtils.inSkyBlock) return
        if (LorenzUtils.skyBlockIsland != IslandType.CRIMSON_ISLE) return
        if (!SkyHanniMod.feature.misc.crimsonIsleReputationHelper) return

        if (event.gui !is GuiChest) return
        val chest = event.gui.inventorySlots as ContainerChest
        val chestName = chest.getInventoryName()

        if (chestName == "Challenges") {
            if (LorenzUtils.skyBlockArea != "Dojo") return
            val dojoQuest = getQuest<DojoQuest>() ?: return
            if (dojoQuest.state != QuestState.ACCEPTED) return

            for (slot in chest.inventorySlots) {
                if (slot == null) continue
                if (slot.slotNumber != slot.slotIndex) continue
                val stack = slot.stack ?: continue
                val itemName = stack.name ?: continue

                if (itemName.contains(dojoQuest.dojoName)) {
                    slot highlight LorenzColor.AQUA
                }
            }
        }
        if (chestName == "Sack of Sacks") {
            val fetchQuest = getQuest<FetchQuest>() ?: return
            if (fetchQuest.state != QuestState.ACCEPTED) return

            val fetchItem = fetchQuest.itemName
            for (slot in chest.inventorySlots) {
                if (slot == null) continue
                if (slot.slotNumber != slot.slotIndex) continue
                val stack = slot.stack ?: continue
                if (stack.getLore().any { it.contains(fetchItem) }) {
                    slot highlight LorenzColor.AQUA
                }
            }
        }
        if (chestName.contains("Nether Sack")) {
            val fetchQuest = getQuest<FetchQuest>() ?: return
            if (fetchQuest.state != QuestState.ACCEPTED) return

            val fetchItem = fetchQuest.itemName
            for (slot in chest.inventorySlots) {
                if (slot == null) continue
                if (slot.slotNumber != slot.slotIndex) continue
                val stack = slot.stack ?: continue
                val itemName = stack.name ?: continue

                if (itemName.contains(fetchItem)) {
                    slot highlight LorenzColor.AQUA
                }
            }
        }
    }

    @SubscribeEvent
    fun onChat(event: LorenzChatEvent) {
        if (!LorenzUtils.inSkyBlock) return
        if (LorenzUtils.skyBlockIsland != IslandType.CRIMSON_ISLE) return
        if (!SkyHanniMod.feature.misc.crimsonIsleReputationHelper) return

        val message = event.message
        if (message == "§aYou completed your Dojo quest! Visit the Town Board to claim the rewards.") {
            val dojoQuest = getQuest<DojoQuest>() ?: return
            dojoQuest.state = QuestState.READY_TO_COLLECT
            update()
        }
        if (message == "§aYou completed your rescue quest! Visit the Town Board to claim the rewards,") {
            val rescueMissionQuest = getQuest<RescueMissionQuest>() ?: return
            rescueMissionQuest.state = QuestState.READY_TO_COLLECT
            update()
        }
    }

    inline fun <reified T : Quest> getQuest() = quests.filterIsInstance<T>().firstOrNull()

    private fun checkInventoryForFetchItem() {
        val fetchQuest = getQuest<FetchQuest>() ?: return
        if (fetchQuest.state != QuestState.ACCEPTED && fetchQuest.state != QuestState.READY_TO_COLLECT) return

        val itemName = fetchQuest.itemName

        val count = InventoryUtils.countItemsInLowerInventory { it.name?.contains(itemName) ?: false }
        updateProcessQuest(fetchQuest, count)
    }

    private fun updateProcessQuest(quest: ProgressQuest, newAmount: Int) {
        var count = newAmount
        val needAmount = quest.needAmount
        if (count > needAmount) {
            count = needAmount
        }
        if (quest.haveAmount == count) return
        LorenzUtils.chat("§e[SkyHanni] ${quest.displayName} progress: $count/$needAmount")

        quest.haveAmount = count
        quest.state = if (count == needAmount) QuestState.READY_TO_COLLECT else QuestState.ACCEPTED
        update()
    }

    @SubscribeEvent
    fun onProfileDataLoad(event: ProfileApiDataLoadedEvent) {
        val profileData = event.profileData
        val sacks = profileData["sacks_counts"]?.asJsonObject ?: return

        sacksCache.clear()

        for ((name, v) in sacks.entrySet()) {
            val amount = v.asLong
            sacksCache[name] = amount
        }
        update()
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!LorenzUtils.inSkyBlock) return
        if (LorenzUtils.skyBlockIsland != IslandType.CRIMSON_ISLE) return
        if (!SkyHanniMod.feature.misc.crimsonIsleReputationHelper) return
        if (!SkyHanniMod.feature.misc.crimsonIsleReputationLocation) return

        for (quest in quests) {
            if (quest is MiniBossQuest) continue
            if (quest.state != QuestState.ACCEPTED) continue
            val location = quest.location ?: continue

            event.drawWaypointFilled(location, LorenzColor.WHITE.toColor())
            event.drawDynamicText(location, quest.displayName, 1.5)
        }

        renderTownBoard(event)
    }

    private fun renderTownBoard(event: RenderWorldLastEvent) {
        if (quests.any { it.state == QuestState.READY_TO_COLLECT || it.state == QuestState.NOT_ACCEPTED }) {
            val location = when (reputationHelper.factionType) {
                FactionType.BARBARIAN -> townBoardBarbarian
                FactionType.MAGE -> townBoardMage

                FactionType.NONE -> return
            }
            event.drawWaypointFilled(location, LorenzColor.WHITE.toColor())
            event.drawDynamicText(location, "Town Board", 1.5)
        }
    }

    fun render(display: MutableList<List<Any>>) {
        val done = quests.count { it.state == QuestState.COLLECTED }
        display.add(Collections.singletonList(""))
        display.add(Collections.singletonList("Daily Quests ($done/5 collected)"))
        if (done != 5) {
            quests.mapTo(display) { renderQuest(it) }
        }
    }

    private fun renderQuest(quest: Quest): List<Any> {
        val type = quest.category.displayName
        val state = quest.state.displayName
        val stateColor = quest.state.color
        val displayName = quest.displayName

        val multipleText = if (quest is ProgressQuest && quest.state != QuestState.COLLECTED) {
            val haveAmount = quest.haveAmount
            val needAmount = quest.needAmount
            " §e$haveAmount§8/§e$needAmount"
        } else {
            ""
        }

        val sacksText = if (quest is FetchQuest && quest.state != QuestState.COLLECTED) {
            val name = quest.itemName.uppercase()
            val amount = sacksCache.getOrDefault(name, 0)
            val needAmount = quest.needAmount
            val amountFormat = LorenzUtils.formatInteger(amount)
            val color = if (amount >= needAmount) {
                "§a"
            } else {
                "§c"
            }
            " §f($color$amountFormat §fin sacks)"
        } else {
            ""
        }

        val stateText = if (quest !is UnknownQuest) {
            "$stateColor[$state] §f"
        } else {
            ""
        }

        val result = mutableListOf<Any>()
        val item = quest.displayItem
        if (item == null) {
            result.add("  $stateText$type: §f$displayName$multipleText$sacksText")
        } else {
            result.add("  $stateText$type: ")
            result.add(NEUItems.readItemFromRepo(item))
            result.add("§f$displayName$multipleText$sacksText")
        }
        return result
    }

    fun finishMiniBoss(miniBoss: CrimsonMiniBoss) {
        val miniBossQuest = getQuest<MiniBossQuest>() ?: return
        if (miniBossQuest.miniBoss == miniBoss) {
            if (miniBossQuest.state == QuestState.ACCEPTED) {
                updateProcessQuest(miniBossQuest, miniBossQuest.haveAmount + 1)
            }
        }
    }

    fun finishKuudra(kuudraTier: KuudraTier) {
        val kuudraQuest = getQuest<KuudraQuest>() ?: return
        if (kuudraQuest.kuudraTier == kuudraTier) {
            //TODO make inline method for this two lines
            if (kuudraQuest.state == QuestState.ACCEPTED) {
                kuudraQuest.state = QuestState.READY_TO_COLLECT
            }
        }
    }

    fun reset() {
        quests.clear()
        latestTrophyFishInInventory = 0
    }

    fun load() {
        reset()

        loader.loadConfig()
        latestTrophyFishInInventory = SkyHanniMod.feature.hidden.crimsonIsleLatestTrophyFishInInventory
    }

    fun saveConfig() {
        SkyHanniMod.feature.hidden.crimsonIsleQuests.clear()
        for (quest in quests) {
            val builder = StringBuilder()
            val internalName = quest.internalName
            builder.append(internalName)
            builder.append(":")
            val state = quest.state
            builder.append(state)

            if (quest is ProgressQuest) {
                val need = quest.needAmount
                val have = quest.haveAmount

                builder.append(":")
                builder.append(need)
                builder.append(":")
                builder.append(have)
            } else {
                builder.append(":0")
            }
            SkyHanniMod.feature.hidden.crimsonIsleQuests.add(builder.toString())
        }

        SkyHanniMod.feature.hidden.crimsonIsleLatestTrophyFishInInventory = latestTrophyFishInInventory
    }
}