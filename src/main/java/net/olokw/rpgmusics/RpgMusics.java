package net.olokw.rpgmusics;

import net.olokw.rpgmusics.Commands.MainCommand;
import net.olokw.rpgmusics.Managers.LoopManager;
import net.olokw.rpgmusics.Managers.RegionManager;
import net.olokw.rpgmusics.Utils.ConfigLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class RpgMusics extends JavaPlugin {

    public static RpgMusics instance;
    private ConfigLoader configLoader;
    private RegionManager regionManager;
    private LoopManager loopManager;

    @Override
    public void onEnable() {


        instance = this;

        regionManager = new RegionManager();
        loopManager = new LoopManager();
        configLoader = new ConfigLoader();

        configLoader.load();
        // Plugin startup logic
        Listeners.register();

        this.getCommand("rpgmusics").setExecutor(new MainCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public LoopManager getLoopManager() {
        return loopManager;
    }

}
