package at.hannibal2.skyhanni.config.features;

import at.hannibal2.skyhanni.config.core.config.Position;
import at.hannibal2.skyhanni.config.core.config.annotations.*;
import com.google.gson.annotations.Expose;

public class Misc {

    @Expose
    @ConfigOption(name = "Pet", desc = "")
    @ConfigEditorAccordion(id = 0)
    public boolean pet = false;

    @Expose
    @ConfigOption(name = "Pet Display", desc = "Show the currently active pet.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 0)
    public boolean petDisplay = false;

    @Expose
    @ConfigOption(name = "Pet Display Position", desc = "")
    @ConfigEditorButton(runnableId = "petDisplay", buttonText = "Edit")
    @ConfigAccordionId(id = 0)
    public Position petDisplayPos = new Position(-111, 221, false, true);

    @Expose
    @ConfigOption(name = "Time", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean time = false;

    @Expose
    @ConfigOption(name = "Real Time", desc = "Show IRL time. Useful while playing in full screen mode.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public boolean realTime = false;

    @Expose
    @ConfigOption(name = "Real Time Position", desc = "")
    @ConfigEditorButton(runnableId = "realTime", buttonText = "Edit")
    @ConfigAccordionId(id = 1)
    public Position realTimePos = new Position(10, 10, false, true);

    @Expose
    @ConfigOption(name = "Hide Armor", desc = "")
    @ConfigEditorAccordion(id = 3)
    public boolean hideArmor = false;

    @Expose
    @ConfigOption(name = "Hide Armor", desc = "Hide other players' armor.")
    @ConfigEditorBoolean(runnableId = "hideArmor")
    @ConfigAccordionId(id = 3)
    public boolean hideArmorEnabled = false;

    @Expose
    @ConfigOption(name = "Own Armor", desc = "Hide your own armor.")
    @ConfigEditorBoolean(runnableId = "hideArmor")
    @ConfigAccordionId(id = 3)
    public boolean hideArmorOwn = true;

    @Expose
    @ConfigOption(name = "Only Helmet", desc = "Only hide the helmet.")
    @ConfigEditorBoolean(runnableId = "hideArmor")
    @ConfigAccordionId(id = 3)
    public boolean hideArmorOnlyHelmet = false;

    @Expose
    @ConfigOption(name = "Damage Splash", desc = "")
    @ConfigEditorAccordion(id = 4)
    public boolean damageSplash = false;

    @Expose
    @ConfigOption(name = "Hide Damage Splash", desc = "Hide all damage splashes anywhere in Skyblock.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 4)
    public boolean hideDamageSplash = false;

    @Expose
    @ConfigOption(name = "Potion Effects", desc = "")
    @ConfigEditorAccordion(id = 5)
    public boolean potionEffects = false;

    @Expose
    @ConfigOption(name = "Non God Pot Effects", desc = "Display the active potion effects that are not part of the god pot.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 5)
    public boolean nonGodPotEffectDisplay = false;

    @Expose
    @ConfigOption(name = "Pot Effects Position", desc = "")
    @ConfigEditorButton(runnableId = "nonGodPotEffect", buttonText = "Edit")
    @ConfigAccordionId(id = 5)
    public Position nonGodPotEffectPos = new Position(10, 10, false, true);

    @Expose
    @ConfigOption(name = "Crimson Reputation Helper", desc = "")
    @ConfigEditorAccordion(id = 6)
    public boolean reputationHelper = false;

    @Expose
    @ConfigOption(name = "Crimson Isle Reputation", desc = "Enable features around Reputation features in the Crimson Isle.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 6)
    public boolean crimsonIsleReputationHelper = true;

    @Expose
    @ConfigOption(name = "Reputation Position", desc = "")
    @ConfigEditorButton(runnableId = "crimsonIsleReputationHelper", buttonText = "Edit")
    @ConfigAccordionId(id = 6)
    public Position crimsonIsleReputationHelperPos = new Position(10, 10, false, true);

    @Expose
    @ConfigOption(name = "Reputation Locations", desc = "Crimson Isles waypoints for locations to get reputation.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 6)
    public boolean crimsonIsleReputationLocation = false;

    @Expose
    @ConfigOption(name = "Tia Relay", desc = "")
    @ConfigEditorAccordion(id = 7)
    public boolean tiaRelay = false;

    @Expose
    @ConfigOption(name = "Tia Relay Waypoint", desc = "Show the next relay waypoint for Tia the Fairy, where maintenance for the abiphone network needs to be done.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 7)
    public boolean tiaRelayNextWaypoint = true;

    @Expose
    @ConfigOption(name = "Tia Relay All", desc = "Show all relay waypoints at once.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 7)
    public boolean tiaRelayAllWaypoints = false;

    @Expose
    @ConfigOption(name = "Tia Relay Helper", desc = "Helps with solving the sound puzzle.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 7)
    public boolean tiaRelayHelper = true;

    @Expose
    @ConfigOption(name = "Tia Relay Mute", desc = "Mutes the sound when close to the relay.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 7)
    public boolean tiaRelayMute = true;

    @Expose
    @ConfigOption(name = "Tps Display", desc = "")
    @ConfigEditorAccordion(id = 8)
    public boolean tpsDisplay = false;

    @Expose
    @ConfigOption(name = "Tps Display", desc = "Show the TPS of the current server, like in Soopy.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 8)
    public boolean tpsDisplayEnabled = false;

    @Expose
    @ConfigOption(name = "Tps Display Position", desc = "")
    @ConfigEditorButton(runnableId = "tpsDisplay", buttonText = "Edit")
    @ConfigAccordionId(id = 8)
    public Position tpsDisplayPosition = new Position(10, 10, false, true);

    @Expose
    @ConfigOption(name = "Particle Hider", desc = "")
    @ConfigEditorAccordion(id = 9)
    public boolean particleHider = false;

    @Expose
    @ConfigOption(name = "Blaze Particles", desc = "Hide blaze particles.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 9)
    public boolean hideBlazeParticles = false;

    @Expose
    @ConfigOption(name = "Fireball Particles", desc = "Hide fireball particles.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 9)
    public boolean hideFireballParticles = true;

    @Expose
    @ConfigOption(name = "Fire Particles", desc = "Hide particles from the fire block.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 9)
    public boolean hideFireBlockParticles = true;

    @Expose
    @ConfigOption(name = "Smoke Particle", desc = "Hide smoke particles.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 9)
    public boolean hideSmokeParticles = false;

    @Expose
    @ConfigOption(name = "Far Particles", desc = "Hide particles that are more than 40 blocks away.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 9)
    public boolean hideFarParticles = true;

    @Expose
    @ConfigOption(name = "Close Redstone Particles", desc = "Hide redstone particles around the player (appear for some potion effects).")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 9)
    public boolean hideCloseRedstoneparticles = true;

    @Expose
    @ConfigOption(name = "Chicken Head Timer", desc = "")
    @ConfigEditorAccordion(id = 10)
    public boolean chickenHeadTimer = false;

    @Expose
    @ConfigOption(name = "Enabled", desc = "Show the cooldown until the next time you can lay an egg with the chicken head.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 10)
    public boolean chickenHeadTimerDisplay = false;

    @Expose
    @ConfigOption(name = "Timer Position", desc = "")
    @ConfigEditorButton(runnableId = "chickenHeadTimer", buttonText = "Edit")
    @ConfigAccordionId(id = 10)
    public Position chickenHeadTimerPosition = new Position(-372, 73, false, true);

    @Expose
    @ConfigOption(name = "Exp Bottles", desc = "Hides all the experience orbs lying on the ground.")
    @ConfigEditorBoolean
    public boolean hideExpBottles = false;

    @Expose
    @ConfigOption(name = "Collection Counter Position", desc = "Tracking the number of items you collect. §cDoes not work with sacks.")
    @ConfigEditorButton(runnableId = "collectionCounter", buttonText = "Edit")
    public Position collectionCounterPos = new Position(10, 10, false, true);

    @Expose
    @ConfigOption(name = "Brewing Stand Overlay", desc = "Display the Item names directly inside the Brewing Stand")
    @ConfigEditorBoolean
    public boolean brewingStandOverlay = true;

    @Expose
    @ConfigOption(name = "Red Scoreboard Numbers", desc = "Hide the red scoreboard numbers at the right side of the screen.")
    @ConfigEditorBoolean
    public boolean hideScoreboardNumbers = false;

    @Expose
    @ConfigOption(name = "Explosions Hider", desc = "Hide explosions.")
    @ConfigEditorBoolean
    public boolean hideExplosions = false;

    @Expose
    @ConfigOption(name = "Fire Overlay Hider", desc = "Hide the fire overlay (Like in Skytils)")
    @ConfigEditorBoolean
    public boolean hideFireOverlay = false;

    @Expose
    @ConfigOption(name = "Config Button", desc = "Add a button to the pause menu to configure SkyHanni.")
    @ConfigEditorBoolean
    public boolean configButtonOnPause = true;

    @ConfigOption(name = "Yaw Snapping", desc = "")
    @ConfigEditorAccordion(id = 6)
    public boolean yawSnappingAccordion = false;

    @Expose
    @ConfigOption(name = "Enable Yaw Snapping", desc = "Align your yaw with certain angles")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 6)
    public boolean yawSnapping = false;

    @Expose
    @ConfigOption(name = "Release Distance", desc = "How much you have to overshoot an angle to release yaw snapping")
    @ConfigEditorSlider(minValue = 0, maxValue = 180, minStep = 1)
    @ConfigAccordionId(id = 6)
    public float yawTightness = 90;

    @Expose
    @ConfigOption(name = "Intervals", desc = "In which intervals do you want to enable yaw snapping (45°, 90°, etc.)")
    @ConfigEditorSlider(minValue = 1, maxValue = 180, minStep = 1)
    @ConfigAccordionId(id = 6)
    public float yawIntervals = 45;


    @Expose
    @ConfigOption(name = "Yaw Overlay", desc = "Display your current yaw over your cursor")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 6)
    public boolean displayYawOverlay = true;
}
}
