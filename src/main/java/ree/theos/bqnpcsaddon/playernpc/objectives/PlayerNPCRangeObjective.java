package ree.theos.bqnpcsaddon.playernpc.objectives;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.objectives.NPCRangeObjective;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import ree.theos.bqnpcsaddon.playernpc.PlayerNPCIntegrator;

/**
 * PlayerNPC implementation of {@link NPCRangeObjective}.
 */
public class PlayerNPCRangeObjective extends NPCRangeObjective {
    /**
     * Creates a new PlayerNPC RangeObjective from the given instruction.
     *
     * @param instruction the user-provided instruction
     * @throws InstructionParseException if the instruction is invalid
     */
    public PlayerNPCRangeObjective(final Instruction instruction) throws InstructionParseException {
        super(instruction, PlayerNPCIntegrator::getSupplierByIDStatic);
    }
}
