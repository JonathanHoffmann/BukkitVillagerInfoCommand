package me.Jonnyfant.VillagerInfoCommand;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class CommandVillagerInfo implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Villager villager = getClosestVillager(player);
            player.sendMessage(assembleInfo(villager));
        }
        return true;
    }

    public String assembleInfo (Villager villager)
    {
        String message = "";
        if(villager==null)
            message+="Could not find a nearby villager.";
        else {
            villager.addPotionEffect(PotionEffectType.GLOWING.createEffect(150, 1));

            if (villager.getCustomName() != null)
                message += "Name: " + villager.getCustomName() + "\n";
            message += "Health: " + villager.getHealth() + "/" + villager.getMaxHealth() + "\n";
            try {
                Location bed = villager.getMemory(MemoryKey.HOME);
                message += "Bed coordinats: X=" + bed.getBlockX() + ", Y=" + bed.getBlockY() + ", Z=" + bed.getBlockZ() + " (" + bed.getWorld().getName() + ")\n";
            }
            catch (Exception e)
            {
                message+="Bed: none\n";
            }
            message += "Type: " + villager.getVillagerType() + "\n";
            message += "Profession: " + villager.getProfession() + "\n";
            try {
                Location workstation = villager.getMemory(MemoryKey.JOB_SITE);
                message += "Workstation coordinats: X=" + workstation.getBlockX() + ", Y=" + workstation.getBlockY() + ", Z=" + workstation.getBlockZ() + " (" + workstation.getWorld().getName() + ")\n";
            }
            catch (Exception e)
            {
                message += "Workstation: none\n";
            }
            message += "Level: " + villager.getVillagerLevel() + "\n";

            message+=inventoryString(villager);
        }
        return message;
    }

    public String inventoryString(Villager villager)
    {
        String inventoryString ="";
        try {
            Inventory inventory = villager.getInventory();
            ItemStack[] content = inventory.getContents();
            for (ItemStack itemStack : content) {
                inventoryString += itemStack.getAmount() + " " + itemStack.getType().name() + "\n";
            }
        }
        catch (Exception e)
        {
            if(inventoryString=="")
                inventoryString+="Empty\n";
        }
        return "\nInventory:\n" + inventoryString;
    }


    public Villager getClosestVillager(Player p) {
        Double shortestDistance = 404.0D;
        Villager nearestVillager = null;
        try {
            List<Entity> near = p.getNearbyEntities(10.0D, 10.0D, 10.0D);

            for (Entity entity : near) {
                if (entity instanceof Villager) {
                    Double distance = entity.getLocation().distance(p.getLocation());
                    if (distance < shortestDistance)
                    {
                        shortestDistance = distance;
                        nearestVillager = (Villager) entity;
                    }
                }
            }
        }
        catch (Exception e)
        {
            //in case no entities are around try catch
        }
        return nearestVillager;
    }
}
