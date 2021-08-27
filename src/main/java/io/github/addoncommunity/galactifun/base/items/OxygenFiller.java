package io.github.addoncommunity.galactifun.base.items;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.addoncommunity.galactifun.Galactifun;
import io.github.addoncommunity.galactifun.api.items.spacesuit.SpaceSuit;
import io.github.addoncommunity.galactifun.api.worlds.PlanetaryWorld;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public final class OxygenFiller extends AContainer {

    public OxygenFiller(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void tick(Block b) {
        // TODO allow if they have oxygen in the area
        PlanetaryWorld world = Galactifun.worldManager().getWorld(b.getWorld());
        if (world != null && world.atmosphere().requiresOxygenTank()) {
            return;
        }

        if (getCharge(b.getLocation()) < getEnergyConsumption()) {
            return;
        }

        BlockMenu inv = BlockStorage.getInventory(b);

        for (int slot : getInputSlots()) {
            ItemStack item = inv.getItemInSlot(slot);

            if (item != null && item.hasItemMeta() && addOxygen(b, inv, slot, item)) {
                return;
            }
        }
    }

    private boolean addOxygen(Block b, BlockMenu inv, int slot, ItemStack item) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem instanceof SpaceSuit suit) {
            ItemMeta meta = item.getItemMeta();
            int oxygen = suit.getOxygen(meta);

            if (oxygen < suit.maxOxygen()) {
                removeCharge(b.getLocation(), getEnergyConsumption());
                suit.setOxygen(meta, oxygen + getSpeed());
                item.setItemMeta(meta);
                return true;
            }
        }

        if (inv.fits(item, getOutputSlots())) {
            inv.pushItem(item, getOutputSlots());
            inv.replaceExistingItem(slot, null);
        }
        return false;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.WATER_BUCKET);
    }

    @Nonnull
    @Override
    public String getMachineIdentifier() {
        return "OXYGEN_FILLER";
    }

}
