package me.gnago.pigcart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public ArrayList<Player> PigCartPlayers = new ArrayList<Player>();

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for (Player plr : PigCartPlayers) {
                    Entity vehiclePig = plr.getVehicle();
                    if (vehiclePig != null && vehiclePig.getType().equals(EntityType.PIG)) {
                        Entity vehicleCart = vehiclePig.getVehicle();
                        if (vehicleCart != null && vehicleCart.getType().equals((EntityType.MINECART)))
                        {
                            if (plr.getCurrentInput().isBackward()) {
                                Vector direction = plr.getFacing().getDirection();
                                vehicleCart.setVelocity(direction);
                                plr.sendMessage(ChatColor.GREEN + "Current direction: " + direction.getX() + ", " + direction.getY() + ", " + direction.getZ());
                            }
                            if (plr.getCurrentInput().isForward()) {
                                Vector direction = plr.getFacing().getOppositeFace().getDirection();
                                vehicleCart.setVelocity(direction);
                                plr.sendMessage(ChatColor.RED + "Current direction: " + direction.getX() + ", " + direction.getY() + ", " + direction.getZ());
                            }
                        }
                    }
                }
            }
        }, 0, 1);
        PluginManager pm = getServer().getPluginManager();
        PigCartListener listener = new PigCartListener(this);
        pm.registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        Player plr = (Player) sender;

        if (sender instanceof Player) {
            String lowerCmd = cmd.getName().toLowerCase();

            switch (lowerCmd) {
                case "pigcarters":
                    plr.sendMessage(ChatColor.LIGHT_PURPLE + "Current PigCarters");
                    for (Player p : PigCartPlayers) {
                        plr.sendMessage(p.getName());
                    }
                    plr.sendMessage();
                    return true;
                default:
                    return true;
            }
        }

        return true;
    }
}
