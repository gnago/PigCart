package me.gnago.pigcart;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.util.Vector;

public class PigCartListener implements Listener {
    public PigCartListener(Main plugin) {
        this.plugin = plugin;
    }

    Main plugin;

    @EventHandler
    public void OnVehicleEnter(VehicleEnterEvent event) {
        Entity plr = event.getEntered();
        if (plr.getType().equals(EntityType.PLAYER))
        {
            Entity pig = event.getVehicle();
            if (pig.getType().equals(EntityType.PIG)) {
                Entity cart = pig.getVehicle();
                if (cart != null && cart.getType().equals(EntityType.MINECART)) {
                    plr.sendMessage("Entered PigCart");
                    plugin.PigCartPlayers.add((Player)plr);
                }
            }
        }
    }

    @EventHandler
    public void OnVehicleExit(VehicleExitEvent event) {
        Entity exited = event.getExited();
        if (exited.getType().equals(EntityType.PLAYER))
        {
            Player plr = (Player)exited;
            Entity pig = event.getVehicle();
            if (pig.getType().equals(EntityType.PIG)) {
                Entity cart = pig.getVehicle();
                if (cart != null && cart.getType().equals(EntityType.MINECART)) {
                    plr.sendMessage("Exited PigCart");
                    if (plugin.PigCartPlayers.removeIf(p -> p.getUniqueId().equals(plr.getUniqueId()))) {
                        plugin.getServer().broadcastMessage("Removed from list");
                    }
                }
            }
        }
    }

    /*
    @EventHandler
    public void OnPlayerInput(PlayerInputEvent event) {
        Entity vehiclePig = event.getPlayer().getVehicle();
        if (vehiclePig != null && vehiclePig.getType().equals(EntityType.PIG)) {
            Entity vehicleCart = vehiclePig.getVehicle();
            if (vehicleCart != null && vehicleCart.getType().equals((EntityType.MINECART)))
            {
                if (event.getInput().isBackward()) {
                    Vector direction = event.getPlayer().getFacing().getOppositeFace().getDirection();
                    vehicleCart.setVelocity(direction);
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Current direction: " + direction.getX() + ", " + direction.getY() + ", " + direction.getZ());
                }
            }
        }
    }*/
}
