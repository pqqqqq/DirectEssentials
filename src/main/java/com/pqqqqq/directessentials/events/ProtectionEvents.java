package com.pqqqqq.directessentials.events;

import com.google.common.base.Optional;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.data.region.Cuboid;
import com.pqqqqq.directessentials.data.region.Region;
import com.pqqqqq.directessentials.wrappers.user.EssentialsUser;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerBreakBlockEvent;
import org.spongepowered.api.event.entity.player.PlayerHarvestBlockEvent;
import org.spongepowered.api.event.entity.player.PlayerInteractBlockEvent;
import org.spongepowered.api.event.entity.player.PlayerPlaceBlockEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

/**
 * Created by Kevin on 2015-05-14.
 */
public class ProtectionEvents {
    private DirectEssentials plugin;

    public ProtectionEvents(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void interact(PlayerInteractBlockEvent event) {
        Player player = event.getEntity();
        Location loc = event.getBlock();

        if (!player.hasPermission("directessentials.region.create")) {
            return;
        }

        Optional<ItemStack> hand = player.getItemInHand();
        if (!hand.isPresent() || hand.get().getItem() != ItemTypes.WOODEN_HOE) {
            return;
        }

        final EssentialsUser user = plugin.getEssentialsGame().getOrCreateUser(player.getUniqueId().toString());

        if (user.isRegionSelectDelay()) { // TODO: Hacky fix for now, remove when fixed
            return;
        }

        Cuboid cuboid = user.getRegionSelection();
        EntityInteractionType type = event.getInteractionType();

        try {
            boolean success = false;
            if (type == EntityInteractionTypes.ATTACK) { // Left click
                cuboid.setAsVec1(loc);
                player.sendMessage(Texts.of(TextColors.AQUA, "Position 1 set (" + cuboid.getSize() + ")"));
                success = true;
            } else if (type == EntityInteractionTypes.USE) { // Right click
                cuboid.setAsVec2(loc);
                player.sendMessage(Texts.of(TextColors.AQUA, "Position 2 set (" + cuboid.getSize() + ")"));
                success = true;
            }

            if (success) {
                event.setCancelled(true);
                user.setRegionSelectDelay(true);

                plugin.getGame().getSyncScheduler().runTaskAfter(plugin, new Runnable() {

                    public void run() {
                        user.setRegionSelectDelay(false);
                    }
                }, 5);
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(Texts.of(TextColors.RED, e.getMessage()));
        }
    }

    @Subscribe(order = Order.LAST)
    public void place(PlayerPlaceBlockEvent event) {
        canBuild(event.getEntity(), event.getBlock(), event);
    }

    @Subscribe(order = Order.LAST)
    public void breakBlock(PlayerBreakBlockEvent event) {
        canBuild(event.getEntity(), event.getBlock(), event);
    }

    @Subscribe(order = Order.LAST)
    public void harvest(PlayerHarvestBlockEvent event) {
        canBuild(event.getEntity(), event.getBlock(), event);
    }

    @Subscribe(order = Order.LAST)
    public void interactBlock(PlayerInteractBlockEvent event) {
        canBuild(event.getEntity(), event.getBlock(), event);
    }

    private boolean canBuild(Player player, Location block, Cancellable event) {
        if (player.hasPermission("directessentials.region.override.*")) {
            return true;
        }

        for (Region region : plugin.getEssentialsGame().getRegions().values()) {
            if (region.getCuboid().contains(block)) {
                if (player.hasPermission("directessentials.region.override." + region.getName())) {
                    return true;
                }

                player.sendMessage(Texts.of(TextColors.RED, "You can't build here."));
                event.setCancelled(true);
                return false;
            }
        }

        return true;
    }
}
