package ree.theos.bqnpcsaddon.znpcsplus.objectives;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.objectives.NPCRangeObjective;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import ree.theos.bqnpcsaddon.znpcsplus.ZNpcsPlusIntegrator;

/**
 * ZNpcsPlus implementation of {@link NPCRangeObjective}.
 */
public class ZNpcsPlusRangeObjective extends NPCRangeObjective {
    /**
     * Creates a new ZNpcsPlus RangeObjective from the given instruction.
     *
     * @param instruction the user-provided instruction
     * @throws InstructionParseException if the instruction is invalid
     */
    public ZNpcsPlusRangeObjective(final Instruction instruction) throws InstructionParseException {
        super(instruction, () -> ZNpcsPlusIntegrator::getSupplierByIDStatic);
    }
}
