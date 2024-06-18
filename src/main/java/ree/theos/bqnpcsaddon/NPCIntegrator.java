package ree.theos.bqnpcsaddon;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.compatibility.Compatibility;
import org.betonquest.betonquest.compatibility.Integrator;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCConversationStarter;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCSupplierStandard;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.conditions.distance.NPCDistanceConditionFactory;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.conditions.location.NPCLocationConditionFactory;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.conditions.region.NPCRegionConditionFactory;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.events.teleport.NPCTeleportEventFactory;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.objectives.NPCInteractObjective;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.objectives.NPCRangeObjective;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.variables.npc.NPCVariableFactory;
import org.betonquest.betonquest.quest.registry.QuestTypeRegistries;
import org.betonquest.betonquest.quest.registry.type.ConditionTypeRegistry;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class NPCIntegrator implements Integrator {
    /**
     * Starts conversations by clicking on the npc.
     */
    private NPCConversationStarter conversationStarter;

    /**
     * The default Constructor.
     */
    public NPCIntegrator() {
    }

    /**
     * @param prefix            the prefix used before any registered name for distinguishing
     * @param supplier
     * @param starter
     * @param interactObjective
     * @param rangeObjective
     */
    protected void hook(final String prefix, final Supplier<NPCSupplierStandard> supplier,
            final Function<BetonQuestLoggerFactory, ? extends NPCConversationStarter> starter,
            final Class<? extends NPCInteractObjective> interactObjective, final Class<? extends NPCRangeObjective> rangeObjective) {
        final BetonQuest plugin = BetonQuest.getInstance();
        final BetonQuestLoggerFactory loggerFactory = plugin.getLoggerFactory();
        conversationStarter = starter.apply(loggerFactory);
        /*
        -- new CitizensWalkingListener();
        -- plugin.registerEvents("movenpc", NPCMoveEvent.class);
        -- plugin.registerEvents("stopnpc", NPCStopEvent.class);
         */

        final QuestTypeRegistries questRegistries = plugin.getQuestRegistries();

        // if ProtocolLib is hooked, start NPCHider
        /*
        if (Compatibility.getHooked().contains("ProtocolLib")) {
            NPCHider.start(loggerFactory.create(NPCHider.class));
            plugin.registerEvents("updatevisibility", UpdateVisibilityNowEvent.class);
        }
         */
        //plugin.registerObjectives("npckill", NPCKillObjective.class);
        plugin.registerObjectives(prefix + "interact", interactObjective);
        plugin.registerObjectives(prefix + "range", rangeObjective);
        questRegistries.getEventTypes().register(prefix + "teleport", new NPCTeleportEventFactory(supplier));
        //plugin.registerConversationIO("chest", CitizensInventoryConvIO.class);
        //plugin.registerConversationIO("combined", CitizensInventoryConvIO.CitizensCombined.class);
        questRegistries.getVariableTypes().register(prefix, new NPCVariableFactory(loggerFactory, supplier));
        final ConditionTypeRegistry conditionTypes = questRegistries.getConditionTypes();
        conditionTypes.register(prefix + "distance", new NPCDistanceConditionFactory(supplier));
        conditionTypes.register(prefix + "location", new NPCLocationConditionFactory(supplier));
        if (Compatibility.getHooked().contains("WorldGuard")) {
            conditionTypes.register(prefix + "region", new NPCRegionConditionFactory(supplier));
        }
    }

    @Override
    public void reload() {
        conversationStarter.reload();
    }

    @Override
    public void close() {
        // Empty
    }
}
