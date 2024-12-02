package me.gnago.pigcart;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.util.Vector;

public class PigCartListener implements Listener {
    public PigCartListener(Main plugin) {

    }

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
    }
}
