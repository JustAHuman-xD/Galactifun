package io.github.addoncommunity.galactifun.core.explorer;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class for exploring the universe through ChestMenus
 * 
 * @author Mooy1
 */
public final class GalacticExplorer {

    private static final Map<UUID, GalacticHolder<?>> history = new HashMap<>();
    
    public static void explore(@Nonnull Player p) {
        GalacticHolder<?> holder = history.computeIfAbsent(p.getUniqueId(), k -> TheUniverse.INSTANCE);

        ChestMenu menu = new ChestMenu(holder.getName());
        
        menu.setEmptySlotsClickable(false);
        
        
    }
    
}
