package ree.theos.bqnpcsaddon.fancynpcs;

import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCIntegrator;
import ree.theos.bqnpcsaddon.fancynpcs.objectives.FancyNpcsInteractObjective;
import ree.theos.bqnpcsaddon.fancynpcs.objectives.FancyNpcsRangeObjective;

import java.util.function.Supplier;

/**
 * Integrator implementation for the FancyNpcs plugin.
 */
public class FancyNpcsIntegrator extends NPCIntegrator<Npc> {
    /**
     * The prefix used before any registered name for distinguishing.
     */
    private static final String PREFIX = "FancyNpcs";

    /**
     * The default Constructor.
     */
    public FancyNpcsIntegrator() {
    }

    public static Supplier<BQNPCAdapter<?>> getSupplierByIDStatic(final String npcId) {
        return () -> {
            final Npc npc = FancyNpcsPlugin.get().getNpcManager().getNpc(npcId);
            return npc == null ? null : new FancyNpcsBQAdapter(npc);
        };
    }

    @Override
    public void hook() {
        hook(PREFIX, () -> FancyNpcsIntegrator::getSupplierByIDStatic,
                loggerFactory -> new FancyNpcsConversationStarter(loggerFactory,
                        loggerFactory.create(FancyNpcsConversationStarter.class)),
                loggerFactory -> new FancyNPCHider(loggerFactory.create(FancyNPCHider.class),
                        FancyNpcsIntegrator::getSupplierByIDStatic, PREFIX),
                FancyNpcsInteractObjective.class, FancyNpcsRangeObjective.class);
    }
}
