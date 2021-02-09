package io.github.addoncommunity.galactifun;

import io.github.addoncommunity.galactifun.api.universe.world.AlienWorld;
import io.github.addoncommunity.galactifun.base.BaseRegistry;
import io.github.addoncommunity.galactifun.core.CoreCategories;
import io.github.addoncommunity.galactifun.core.commands.AlienSpawnCommand;
import io.github.addoncommunity.galactifun.core.commands.GalactiportCommand;
import io.github.addoncommunity.galactifun.core.commands.GenSphereCommand;
import io.github.addoncommunity.galactifun.core.listener.AlienListener;
import io.github.addoncommunity.galactifun.core.listener.CelestialListener;
import io.github.addoncommunity.galactifun.core.profile.GalacticProfile;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.command.CommandManager;
import io.github.mooy1.infinitylib.config.ConfigUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class Galactifun extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static Galactifun instance;
    
    @Override
    public void onEnable() {
        instance = this;

        PluginUtils.setup("galactifun", this, "Slimefun-Addon-Community/Galactifun/master", getFile());

        CommandManager.setup("galactifun", "galactifun.admin", "/gf, /galactic",
                new GalactiportCommand(), new AlienSpawnCommand(), new GenSphereCommand()
        );
        
        CoreCategories.setup(this);
        
        BaseRegistry.setup();
        
        new CelestialListener();
        new AlienListener();
        
        scheduleTasks();
        
        // log after startup
        PluginUtils.runSync(() -> PluginUtils.log(
                "",
                "################# Galactifun " + getPluginVersion() + " #################",
                "",
                "Loaded " + AlienWorld.getEnabled().size() + " worlds: ",
                AlienWorld.getEnabled().toString(),
                "",
                "Galactifun is open source, you can contribute or report bugs at: ",
                getBugTrackerURL(),
                "Join the Slimefun Addon Community Discord: Discord.gg/V2cJR9ADFU",
                "",
                "###################################################",
                ""
        ));
    }
    
    private static void scheduleTasks() {
        PluginUtils.scheduleRepeatingSync(AlienWorld::tickWorlds, 100);
        PluginUtils.scheduleRepeatingSync(AlienWorld::tickAliens, ConfigUtils.getInt("aliens.tick-interval", 1, 20, 4));
        PluginUtils.scheduleRepeatingSync(GalacticProfile::saveAll, 12000);
    }

    @Override
    public void onDisable() {
        instance = null;

        GalacticProfile.closeAll();
        GalacticProfile.saveAll();
        
    }

    @Override
    public String getBugTrackerURL() {
        return "Slimefun-Addon-Community/Galactifun/master/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
    
}
