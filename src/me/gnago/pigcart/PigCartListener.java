package me.gnago.pigcart;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class PigCartListener implements Listener {
    public PigCartListener(Main plugin) {
        this.plugin = plugin;
    }

    private final Main plugin;

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
}
