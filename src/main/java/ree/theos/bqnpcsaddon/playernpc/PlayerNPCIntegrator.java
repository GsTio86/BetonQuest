package ree.theos.bqnpcsaddon.playernpc;

import dev.sergiferry.playernpc.PlayerNPCPlugin;
import dev.sergiferry.playernpc.api.NPC;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.BQNPCAdapter;
import org.betonquest.betonquest.compatibility.npcs.abstractnpc.NPCIntegrator;
import ree.theos.bqnpcsaddon.playernpc.objectives.PlayerNPCInteractObjective;
import ree.theos.bqnpcsaddon.playernpc.objectives.PlayerNPCRangeObjective;

import java.util.function.Supplier;

/**
 * Integrator implementation for the
 * <a href="https://www.spigotmc.org/resources/93625/">PlayerNPC plugin</a>.
 */
public class PlayerNPCIntegrator extends NPCIntegrator<NPC> {
    /**
     * The prefix used before any registered name for distinguishing.
     */
    private static final String PREFIX = "PlayerNPC";

    /**
     * The default Constructor.
     */
    public PlayerNPCIntegrator() {
    }

    @SuppressWarnings("deprecation")
    public static Supplier<BQNPCAdapter<?>> getSupplierByIDStatic(final String npcId) {
        return () -> {
            final NPC npc = PlayerNPCPlugin.getInstance().getNPCLib().getGlobalNPC(npcId);
            return npc == null ? null : new PlayerNPCBQAdapter(npc);
        };
    }

    @Override
    public void hook() {
        hook(PREFIX, () -> PlayerNPCIntegrator::getSupplierByIDStatic,
                loggerFactory -> new PlayerNPCConversationStarter(loggerFactory,
                        loggerFactory.create(PlayerNPCConversationStarter.class)),
                null, PlayerNPCInteractObjective.class, PlayerNPCRangeObjective.class);
    }
}
