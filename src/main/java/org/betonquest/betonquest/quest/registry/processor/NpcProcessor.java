package org.betonquest.betonquest.quest.registry.processor;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.NpcInteractEvent;
import org.betonquest.betonquest.api.config.quest.QuestPackage;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.profile.OnlineProfile;
import org.betonquest.betonquest.api.quest.QuestException;
import org.betonquest.betonquest.api.quest.npc.Npc;
import org.betonquest.betonquest.api.quest.npc.NpcWrapper;
import org.betonquest.betonquest.api.quest.npc.feature.NpcConversation;
import org.betonquest.betonquest.api.quest.npc.feature.NpcHider;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.conversation.CombatTagger;
import org.betonquest.betonquest.exception.ObjectNotFoundException;
import org.betonquest.betonquest.id.ConversationID;
import org.betonquest.betonquest.id.NpcID;
import org.betonquest.betonquest.objective.EntityInteractObjective.Interaction;
import org.betonquest.betonquest.quest.registry.type.NpcTypeRegistry;
import org.betonquest.betonquest.util.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Stores Npcs and starts Npc conversations.
 */
public class NpcProcessor extends TypedQuestProcessor<NpcID, NpcWrapper<?>> {
    /**
     * The section in which the assignments from Npcs inside conversations are stored.
     */
    private static final String NPC_SECTION = "npcs";

    /**
     * The {@link BetonQuestLoggerFactory} to use for creating {@link BetonQuestLogger} instances.
     */
    private final BetonQuestLoggerFactory loggerFactory;

    /**
     * Plugin to load config.
     */
    private final BetonQuest plugin;

    /**
     * Stores the last time the player interacted with an NPC.
     */
    private final Map<UUID, Long> npcInteractionLimiter = new HashMap<>();

    /**
     * Stores the conversations assigned to NPCs via the configuration.
     * The key could either be a Npcs name or its ID, depending on the configuration.
     */
    private final Map<String, ConversationID> assignedConversations = new HashMap<>();

    /**
     * Hider for Npcs.
     */
    private final NpcHider npcHider;

    /**
     * The minimum time between two interactions with an NPC.
     */
    private int interactionLimit;

    /**
     * If left click interactions should also trigger conversation starts.
     */
    private boolean acceptNpcLeftClick;

    /**
     * Create a new Quest Npc Processor to store them.
     *
     * @param log           the custom logger for this class
     * @param npcTypes      the available npc types
     * @param loggerFactory the logger factory used to create logger for the started conversations
     * @param plugin        the plugin to load config
     */
    public NpcProcessor(final BetonQuestLogger log, final NpcTypeRegistry npcTypes, final BetonQuestLoggerFactory loggerFactory, final BetonQuest plugin) {
        super(log, npcTypes, "Npcs", "npc_definitions");
        this.loggerFactory = loggerFactory;
        this.plugin = plugin;
        interactionLimit = plugin.getPluginConfig().getInt("npcInteractionLimit", 500);
        plugin.getServer().getPluginManager().registerEvents(new NpcListener(), plugin);
        this.npcHider = new NpcHider(loggerFactory.create(NpcHider.class), this, plugin, Config.getPackages().values());
    }

    /**
     * Loads the npc references to start the conversation on interaction with them.
     *
     * @param convID      the conversation to start
     * @param convSection the section to load the references
     */
    public void loadConversation(final ConversationID convID, final ConfigurationSection convSection) {
        if (convSection.isString(NPC_SECTION)) {
            for (final String string : Objects.requireNonNull(convSection.getString(NPC_SECTION)).split(",")) {
                try {
                    final NpcID identifier = new NpcID(convID.getPackage(), string);
                    assignedConversations.put(identifier.getInstruction().toString(), convID);
                } catch (final ObjectNotFoundException exception) {
                    log.warn(convID.getPackage(), "Error while loading Npc in conversation " + convID.getFullID() + "': " + exception.getMessage()
                            + "! The conversation will still load but the Npc won't start it.", exception);
                }
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        interactionLimit = plugin.getPluginConfig().getInt("npcInteractionLimit", 500);
        acceptNpcLeftClick = plugin.getPluginConfig().getBoolean("acceptNPCLeftClick");
        npcHider.reload(Config.getPackages().values());
    }

    @Override
    protected NpcID getIdentifier(final QuestPackage pack, final String identifier) throws ObjectNotFoundException {
        return new NpcID(pack, identifier);
    }

    /**
     * Gets a Npc by its id.
     *
     * @param npcID the id of the Npc
     * @return the wrapper to get the actual
     * @throws QuestException when there is no Npc with that id
     */
    public Npc<?> getNpc(final NpcID npcID) throws QuestException {
        final NpcWrapper<?> npcWrapper = values.get(npcID);
        if (npcWrapper == null) {
            throw new QuestException("Tried to get npc '" + npcID.getFullID() + "' but it is not loaded! Check for errors on /bq reload!");
        }
        return npcWrapper.getNpc();
    }

    /**
     * The logic that determines if an NPC interaction starts a conversation.
     *
     * @param clicker    the player who clicked the NPC
     * @param identifier the identifier for the Npc as used in the definition section
     * @param npc        the npc which was interacted with
     * @return if a conversation is started and the interact event should be cancelled
     */
    public boolean interactLogic(final Player clicker, final String identifier, final Npc<?> npc) {
        if (!clicker.hasPermission("betonquest.conversation")) {
            return false;
        }
        final UUID playerUUID = clicker.getUniqueId();

        final Long lastClick = npcInteractionLimiter.get(playerUUID);
        final long currentClick = new Date().getTime();
        if (lastClick != null && lastClick + interactionLimit >= currentClick) {
            return false;
        }
        npcInteractionLimiter.put(playerUUID, currentClick);

        final OnlineProfile onlineProfile = PlayerConverter.getID(clicker);
        if (CombatTagger.isTagged(onlineProfile)) {
            try {
                Config.sendNotify(null, onlineProfile, "busy", "busy,error");
            } catch (final QuestException e) {
                log.warn("The notify system was unable to play a sound for the 'busy' category. Error was: '" + e.getMessage() + "'", e);
            }
            return false;
        }

        return startConversation(clicker, identifier, npc, onlineProfile);
    }

    private boolean startConversation(final Player clicker, final String identifier, final Npc<?> npc, final OnlineProfile onlineProfile) {
        final boolean npcsByName = Boolean.parseBoolean(Config.getConfigString("citizens_npcs_by_name"));
        final String selector = npcsByName ? npc.getName() : identifier;
        final ConversationID conversationID = assignedConversations.get(selector);

        if (conversationID == null) {
            log.debug("Player '" + clicker.getName() + "' clicked Npc '" + selector + "' but there is no conversation assigned to it.");
            return false;
        } else {
            log.debug("Player '" + clicker.getName() + "' clicked Npc '" + selector + "' and started conversation '" + conversationID.getFullID() + "'.");
            new NpcConversation<>(loggerFactory.create(NpcConversation.class), onlineProfile, conversationID, npc.getLocation(), npc);
            return true;
        }
    }

    /**
     * Gets the NpcHider.
     *
     * @return the active npc hider
     */
    public NpcHider getNpcHider() {
        return npcHider;
    }

    /**
     * Listener for Conversation starting and Hiding with {@link Npc}s.
     */
    private class NpcListener implements Listener {
        /**
         * The default Constructor.
         */
        public NpcListener() {

        }

        /**
         * Attempts to start conversations on Npc interactions.
         *
         * @param event the interact event
         */
        @EventHandler(ignoreCancelled = true)
        public void onInteract(final NpcInteractEvent event) {
            if (event.getInteraction() == Interaction.LEFT && !acceptNpcLeftClick) {
                return;
            }
            if (interactLogic(event.getPlayer(), event.getNpcIdentifier(), event.getNpc())) {
                event.setCancelled(true);
            }
        }

        /**
         * Applies the visibility on Player join.
         *
         * @param event the event to listen
         */
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerJoin(final PlayerJoinEvent event) {
            Bukkit.getScheduler().runTask(BetonQuest.getInstance(), () -> npcHider.applyVisibility(PlayerConverter.getID(event.getPlayer())));
        }
    }
}
