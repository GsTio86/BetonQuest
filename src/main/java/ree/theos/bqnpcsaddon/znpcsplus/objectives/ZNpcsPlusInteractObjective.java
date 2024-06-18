package ree.theos.bqnpcsaddon.znpcsplus.objectives;

import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.objectives.NPCInteractObjective;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.objectives.EntityInteractObjective;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import static org.betonquest.betonquest.objectives.EntityInteractObjective.Interaction.*;

/**
 * ZNpcsPlus implementation of {@link NPCInteractObjective}.
 */
public class ZNpcsPlusInteractObjective extends NPCInteractObjective {
    /**
     * Creates a new ZNpcsPlus NPC Interact Objective from the given instruction.
     *
     * @param instruction the user-provided instruction
     * @throws InstructionParseException if the instruction is invalid
     */
    public ZNpcsPlusInteractObjective(final Instruction instruction) throws InstructionParseException {
        super(instruction);
    }

    /**
     * Handles click events.
     *
     * @param event the event provided by the NPC plugin
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(final NpcInteractEvent event) {
        final boolean cancel = onNPCClick(event.getEntry().getId(), convert(event.getClickType()), event.getPlayer());
        if (cancel) {
            event.setCancelled(true);
        }
    }

    private EntityInteractObjective.Interaction convert(final InteractionType interactionType) {
        return switch (interactionType) {
            case LEFT_CLICK -> LEFT;
            case RIGHT_CLICK -> RIGHT;
            case ANY_CLICK -> ANY;
        };
    }
}
