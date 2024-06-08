package org.betonquest.betonquest.quest.event.drop;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.Instruction.Item;
import org.betonquest.betonquest.api.common.function.Selector;
import org.betonquest.betonquest.api.common.function.Selectors;
import org.betonquest.betonquest.api.quest.action.Action;
import org.betonquest.betonquest.api.quest.action.PlayerAction;
import org.betonquest.betonquest.api.quest.action.PlayerActionFactory;
import org.betonquest.betonquest.api.quest.action.StaticAction;
import org.betonquest.betonquest.api.quest.action.StaticActionFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.OnlineProfileGroupStaticEventAdapter;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadAction;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Factory to create {@link PlayerAction}s that drop items from instructions.
 */
public class DropActionFactory implements PlayerActionFactory, StaticActionFactory {
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
     * Creates the drop event factory.
     *
     * @param server    server to use
     * @param scheduler scheduler to use
     * @param plugin    plugin to use
     */
    public DropActionFactory(final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    private static Item[] parseItemList(final Instruction instruction) throws InstructionParseException {
        final Item[] items = instruction.getItemListArgument("items");
        if (items.length == 0) {
            throw new InstructionParseException("No items to drop defined");
        }
        return items;
    }

    private static Selector<Location> parseLocationSelector(final Instruction instruction) throws InstructionParseException {
        return instruction.getLocationArgument("location")
                .map(loc -> (Selector<Location>) loc::getLocation)
                .orElse(Selectors.fromPlayer(Player::getLocation));
    }

    @Override
    public StaticAction parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        if (instruction.hasArgument("location")) {
            return parseEvent(instruction);
        } else {
            return new OnlineProfileGroupStaticEventAdapter(PlayerConverter::getOnlineProfiles, parseEvent(instruction));
        }
    }

    @Override
    public Action parseEvent(final Instruction instruction) throws InstructionParseException {
        final Item[] items = parseItemList(instruction);
        final Selector<Location> location = parseLocationSelector(instruction);

        return new PrimaryServerThreadAction(new DropPlayerAction(items, location), server, scheduler, plugin);
    }
}
