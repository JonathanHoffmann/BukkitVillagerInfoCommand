package me.Jonnyfant.VillagerInfoCommand;

import org.bukkit.plugin.java.JavaPlugin;

public class VillagerInfoCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("VillagerInfo").setExecutor(new CommandVillagerInfo());
    }
}
