package org.betonquest.betonquest.quest.event.item;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.VariableNumber;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredPlayerAction;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadPlayerAction;
import org.betonquest.betonquest.quest.event.point.Point;
import org.bukkit.Server;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

/**
 * Factory for the item durability event.
 */
public class ItemDurabilityEventFactory implements PlayerActionFactory {
    /**
     * Factory to create custom {@link BetonQuestLogger} instance for the event.
     */
    private final BetonQuestLoggerFactory loggerFactory;

    /**
     * Server to use for syncing to the primary server thread.
     */
    private final Server server;

    /**
     * Scheduler to use for syncing to the primary server thread.
     */
    private final BukkitScheduler scheduler;

    /**
     * Plugin to use for syncing to the primary server thread.
     */
    private final Plugin plugin;

    /**
     * Create the item durability event factory.
     *
     * @param loggerFactory logger factory to use
     * @param server        server to use
     * @param scheduler     scheduler to use
     * @param plugin        plugin to use
     */
    public ItemDurabilityEventFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.loggerFactory = loggerFactory;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        final EquipmentSlot slot = instruction.getEnum(EquipmentSlot.class);
        final Point operation = instruction.getEnum(Point.class);
        final VariableNumber amount = instruction.getVarNum();
        final boolean ignoreUnbreakable = instruction.hasArgument("ignoreUnbreakable");
        final boolean ignoreEvents = instruction.hasArgument("ignoreEvents");
        final ItemDurabilityPlayerAction event = new ItemDurabilityPlayerAction(slot, operation, amount, ignoreUnbreakable, ignoreEvents, new Random());
        return new PrimaryServerThreadPlayerAction(new OnlineProfileRequiredPlayerAction(loggerFactory.create(event.getClass()), event, instruction.getPackage()),
                server, scheduler, plugin);
    }
}
