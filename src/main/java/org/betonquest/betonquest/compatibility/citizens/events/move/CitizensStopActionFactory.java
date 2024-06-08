package org.betonquest.betonquest.compatibility.citizens.events.move;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.CallStaticPlayerActionAdapter;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadStaticEvent;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Factory for {@link CitizensStopEvent} from the {@link Instruction}.
 */
public class CitizensStopActionFactory implements PlayerActionFactory, StaticActionFactory {
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
     * Move Controller where to stop the NPC movement.
     */
    private final CitizensMoveController citizensMoveController;

    /**
     * Create a new NPCTeleportEventFactory.
     *
     * @param server                 the server to use for syncing to the primary server thread
     * @param scheduler              the scheduler to use for syncing to the primary server thread
     * @param plugin                 the plugin to use for syncing to the primary server thread
     * @param citizensMoveController the move controller where to stop the NPC movement
     */
    public CitizensStopActionFactory(final Server server, final BukkitScheduler scheduler, final Plugin plugin, final CitizensMoveController citizensMoveController) {
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
        this.citizensMoveController = citizensMoveController;
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        final int npcId = instruction.getInt();
        return new PrimaryServerThreadStaticEvent(new CitizensStopEvent(npcId, citizensMoveController), server, scheduler, plugin);
    }

    @Override
    public PlayerAction parseEvent(final Instruction instruction) throws InstructionParseException {
        return new CallStaticPlayerActionAdapter(parseStaticEvent(instruction));
    }
}
