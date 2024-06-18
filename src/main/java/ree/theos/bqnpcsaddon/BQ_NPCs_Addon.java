package ree.theos.bqnpcsaddon;

import org.betonquest.betonquest.compatibility.RegisterHooksEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ree.theos.bqnpcsaddon.fancynpcs.FancyNpcsIntegrator;

/**
 * Plugin that adds support for different NPC plugins.
 * <p>
 * Since this is not the main BQ plugin you can break things as you want :)
 */
public final class BQ_NPCs_Addon extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        // Plugin startup logic
    }

    @EventHandler
    public void onHookCollect(final RegisterHooksEvent event) {
        event.register("FancyNpcs", FancyNpcsIntegrator.class, this);
    }
}
