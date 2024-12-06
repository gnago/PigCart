package me.gnago.pigcart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Input;
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
    private final static double MAX_SPEED = 1.2; //Measured in m/tick. Powered carts are 8m/s, which is 0.4m/t

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
                            if (currInput.isBackward() || currInput.isForward() || currInput.isLeft() || currInput.isRight()) {
                                double yaw = 0;
                                //rotation yaw. Used for rotating the cart
                                float rYaw = 0;
                                int rYawCount = 0;

                                Vector direction = new Vector(0, 0, 0);

                                if (currInput.isBackward()) {
                                    rYaw += plr.getLocation().getYaw() + 90;
                                    rYawCount++;
                                    yaw = ((plr.getLocation().getYaw() + 90) * Math.PI) / 180;
                                    direction = direction.add(
                                            new Vector(Math.cos(yaw), 0, Math.sin(yaw)));
                                }
                                if (currInput.isForward()) {
                                    rYaw += plr.getLocation().getYaw() - 90;
                                    rYawCount++;
                                    yaw = ((plr.getLocation().getYaw() - 90) * Math.PI) / 180;
                                    direction = direction.add(
                                            new Vector(Math.cos(yaw), 0, Math.sin(yaw)));
                                }
                                if (currInput.isLeft()) {
                                    rYaw += plr.getLocation().getYaw() + 180;
                                    rYawCount++;
                                    yaw = ((plr.getLocation().getYaw() + 180) * Math.PI) / 180;
                                    direction = direction.add(
                                            new Vector(Math.cos(yaw), 0, Math.sin(yaw)));
                                }
                                if (currInput.isRight()) {
                                    rYaw += plr.getLocation().getYaw();
                                    rYawCount++;
                                    yaw = ((plr.getLocation().getYaw()) * Math.PI) / 180;
                                    direction = direction.add(
                                            new Vector(Math.cos(yaw), 0, Math.sin(yaw)));
                                }

                                //this will never happen, but it doesn't hurt to have a DivideByZero protection
                                if (rYawCount == 0)
                                    return;

                                //Accelerate in the direction
                                Vector currentVelocity = vehicleCart.getVelocity();
                                if (Math.abs(currentVelocity.getX()) < MAX_SPEED)
                                    currentVelocity.setX(ACCELERATION_RATE * direction.getX() + currentVelocity.getX());
                                if (Math.abs(currentVelocity.getZ()) < MAX_SPEED)
                                    currentVelocity.setZ(ACCELERATION_RATE * direction.getZ() + currentVelocity.getZ());
                                vehicleCart.setVelocity(currentVelocity);
                                vehicleCart.setRotation(rYaw/rYawCount, 0);

                                /*//debug
                                plr.sendMessage(ChatColor.AQUA + "Current yaw: " + plr.getLocation().getYaw());
                                plr.sendMessage(ChatColor.AQUA + "Current direction: " + direction.getX() + ", " + direction.getY() + ", " + direction.getZ());
                                plr.sendMessage(ChatColor.GREEN + "Current velocity: " + currentVelocity.getX() + ", " + currentVelocity.getY() + ", " + currentVelocity.getZ());
                                //*/
                            }
                            else
                                return;

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
}
