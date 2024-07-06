package ree.theos.bqnpcsaddon.znpcsplus;

import lol.pyr.znpcsplus.api.NpcApiProvider;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCIntegrator;
import ree.theos.bqnpcsaddon.znpcsplus.objectives.ZNpcsPlusInteractObjective;
import ree.theos.bqnpcsaddon.znpcsplus.objectives.ZNpcsPlusRangeObjective;

import java.util.function.Supplier;

/**
 * Integrator implementation for the
 * <a href="https://www.spigotmc.org/resources/znpcsplus.109380/">ZNPCsPlus plugin</a>.
 */
public class ZNpcsPlusIntegrator extends NPCIntegrator<NpcEntry> {
    /**
     * The prefix used before any registered name for distinguishing.
     */
    private static final String PREFIX = "ZNPCsPlus";

    /**
     * The default Constructor.
     */
    public ZNpcsPlusIntegrator() {
    }

    public static Supplier<BQNPCAdapter<?>> getSupplierByIDStatic(final String npcId) {
        return () -> {
            final NpcEntry npcEntry = NpcApiProvider.get().getNpcRegistry().getById(npcId);
            return npcEntry == null ? null : new ZNpcsPlusBQAdapter(npcEntry);
        };
    }

    @Override
    public void hook() {
        hook(PREFIX, () -> ZNpcsPlusIntegrator::getSupplierByIDStatic,
                loggerFactory -> new ZNpcsPlusConversationStarter(loggerFactory,
                        loggerFactory.create(ZNpcsPlusConversationStarter.class)),
                loggerFactory -> new ZNpcsPlusHider(loggerFactory.create(ZNpcsPlusHider.class),
                        ZNpcsPlusIntegrator::getSupplierByIDStatic, PREFIX),
                ZNpcsPlusInteractObjective.class, ZNpcsPlusRangeObjective.class);
    }
}
