package org.betonquest.betonquest.compatibility.effectlib;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.DynamicLocation;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.api.quest.npc.Npc;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.id.NpcID;
import org.betonquest.betonquest.instruction.variable.location.VariableLocation;
import org.betonquest.betonquest.quest.registry.processor.NpcProcessor;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link BukkitRunnable} that shows an EffectLib effect to all players that meet the required conditions.
 */
public class EffectLibRunnable extends BukkitRunnable {
    /**
     * Custom {@link BetonQuestLogger} instance for this class.
     */
    private final BetonQuestLogger log;

    /**
     * Effect manager which will create and control the particles.
     */
    private final EffectManager manager;

    /**
     * The configuration of the effect to show.
     */
    private final EffectConfiguration effectConfiguration;

    /**
     * Processor to get npc.
     */
    private final NpcProcessor npcProcessor;

    /**
     * All player profiles that meet the conditions for this classes' effect.
     */
    private List<OnlineProfile> activeProfiles;

    /**
     * The last game tick the conditions were checked on.
     */
    private int lastConditionCheckTick;

    /**
     * Constructs this runnable with the given effect.
     *
     * @param log                 the logger that will be used for logging
     * @param manager             the effect manager which will create and control the particles
     * @param effectConfiguration the effect to show
     * @param npcProcessor        processor to get npc
     */
    public EffectLibRunnable(final BetonQuestLogger log, final EffectManager manager, final EffectConfiguration effectConfiguration, final NpcProcessor npcProcessor) {
        super();
        this.log = log;
        this.manager = manager;
        this.effectConfiguration = effectConfiguration;
        this.npcProcessor = npcProcessor;
        this.activeProfiles = new ArrayList<>();
    }

    @Override
    public void run() {
        if (Bukkit.getCurrentTick() - lastConditionCheckTick >= effectConfiguration.conditionCheckInterval()) {
            activeProfiles = checkActiveEffects();
            lastConditionCheckTick = Bukkit.getCurrentTick();
        }
        activateEffects(activeProfiles);
    }

    private List<OnlineProfile> checkActiveEffects() {
        final List<OnlineProfile> activePlayerEffects = new ArrayList<>();
        for (final OnlineProfile onlineProfile : PlayerConverter.getOnlineProfiles()) {
            if (BetonQuest.conditions(onlineProfile, effectConfiguration.conditions())) {
                activePlayerEffects.add(onlineProfile);
            }
        }
        return activePlayerEffects;
    }

    private void activateEffects(final List<OnlineProfile> activePlayers) {
        for (final OnlineProfile currentPlayer : activePlayers) {
            if (!effectConfiguration.npcs().isEmpty()) {
                runNPCEffects(currentPlayer, effectConfiguration);
            }
            if (!effectConfiguration.locations().isEmpty()) {
                runLocationEffects(currentPlayer, effectConfiguration);
            }
        }
    }

    private void runNPCEffects(final OnlineProfile profile, final EffectConfiguration effect) {
        for (final NpcID npcId : effect.npcs()) {
            final Npc<?> npc;
            try {
                npc = npcProcessor.getNpc(npcId);
            } catch (final QuestRuntimeException exception) {
                log.debug("Could not get Npc for id '" + npcId.getFullID() + "' in effects!", exception);
                continue;
            }
            final Player player = profile.getPlayer();

            if (!npc.getLocation().getWorld().equals(player.getWorld()) || npcProcessor.getNpcHider().isHidden(npcId, profile)) {
                continue;
            }

            final Entity entity = npc.getEntity();
            final DynamicLocation location = entity == null ? new DynamicLocation(npc.getLocation()) : new DynamicLocation(entity);

            manager.start(effect.effectClass(), effect.settings(), location,
                    new DynamicLocation(null, null), (ConfigurationSection) null, player);
        }
    }

    private void runLocationEffects(final OnlineProfile profile, final EffectConfiguration effect) {
        for (final VariableLocation variableLocation : effect.locations()) {
            final Location location;
            try {
                location = variableLocation.getValue(profile);
                manager.start(effect.effectClass(), effect.settings(), location, profile.getPlayer());
            } catch (final QuestRuntimeException exception) {
                log.warn("Error while resolving a location of an EffectLib particle effect of type '" + effect.effectClass() + "'. Check that your location (variables) are correct. Error:", exception);
            }
        }
    }
}
