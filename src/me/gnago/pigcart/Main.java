package me.gnago.pigcart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Input;
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

    public ArrayList<Player> PigCartPlayers = new ArrayList<>();

    private final static double ACCELERATION_RATE = 0.025;
    private final static double MAX_SPEED = 1.2; //powered carts are 8m/s, which is 0.4m/t

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
                            Input currInput = plr.getCurrentInput();
                            if (currInput.isBackward() || currInput.isForward()) {
                                Vector direction;
                                if (currInput.isBackward())
                                    direction = plr.getFacing().getDirection();
                                else if (currInput.isForward())
                                    direction = plr.getFacing().getOppositeFace().getDirection();
                                else
                                    return;

                                //Accelerate in the direction
                                Vector currentVelocity = vehicleCart.getVelocity();
                                if (currentVelocity.getX() < MAX_SPEED)
                                    currentVelocity.setX(ACCELERATION_RATE * direction.getX() + currentVelocity.getX());
                                if (currentVelocity.getZ() < MAX_SPEED)
                                    currentVelocity.setZ(ACCELERATION_RATE * direction.getZ() + currentVelocity.getZ());
                                vehicleCart.setVelocity(currentVelocity);
                                plr.sendMessage(ChatColor.GREEN + "Current velocity: " + currentVelocity.getX() + ", " + currentVelocity.getY() + ", " + currentVelocity.getZ());
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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
