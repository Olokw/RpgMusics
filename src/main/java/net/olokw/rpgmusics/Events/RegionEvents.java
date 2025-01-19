package net.olokw.rpgmusics.Events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.netzkronehd.wgregionevents.events.RegionEnterEvent;
import de.netzkronehd.wgregionevents.events.RegionEnteredEvent;
import de.netzkronehd.wgregionevents.events.RegionLeaveEvent;
import de.netzkronehd.wgregionevents.events.RegionLeftEvent;
import net.olokw.rpgmusics.RpgMusics;
import net.olokw.rpgmusics.Utils.LoopConfig;
import net.olokw.rpgmusics.Utils.RegionConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class RegionEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (getMusicRegion(e.getPlayer().getLocation()) == null){
            for (RegionConfig regionConfig : RpgMusics.instance.getRegionManager().regions){
                if (regionConfig.getRegionName().equalsIgnoreCase("__global__") && regionConfig.getRegionWorld().equalsIgnoreCase(e.getPlayer().getWorld().getName())){
                    tryPlayMusic(e.getPlayer(), regionConfig);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onRegionEnter(RegionEnterEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(e.getPlayer().getWorld())) == null) return;

                RegionConfig regionConfig = getMusicRegion(e.getPlayer().getLocation());

                if (regionConfig != null) tryPlayMusic(e.getPlayer(), regionConfig);

            }
        }.runTaskLater(RpgMusics.instance, 1);
    }

    private void tryPlayMusic(Player p, RegionConfig regionConfig){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (RpgMusics.instance.getLoopManager().tasks.containsKey(p.getUniqueId())) {
                    LoopConfig currentLoopConfig = RpgMusics.instance.getLoopManager().tasks.get(p.getUniqueId());

                    if (!new HashSet<>(currentLoopConfig.getMusicConfigList()).containsAll(regionConfig.getMusicConfigList())
                            && regionConfig.getAvailableMusicsSize(p.getWorld().getTime()) > 0) {

                        if (!currentLoopConfig.getActualMusic().equals(regionConfig.getMusicConfigList().get(0).getMusicName())) {
                            p.stopSound(currentLoopConfig.getActualMusic(), SoundCategory.RECORDS);
                            RpgMusics.instance.getLoopManager().stopTimer(p);
                            RpgMusics.instance.getLoopManager().tasks.remove(p.getUniqueId());
                        }
                    }
                }

                if (!RpgMusics.instance.getLoopManager().tasks.containsKey(p.getUniqueId())) {
                    String currentMusic = RpgMusics.instance.getLoopManager().tasks.containsKey(p.getUniqueId())
                            ? RpgMusics.instance.getLoopManager().tasks.get(p.getUniqueId()).getActualMusic()
                            : null;

                    String regionMusic = regionConfig.getMusicConfigList().get(0).getMusicName();

                    if (!regionMusic.equals(currentMusic)) {
                        RpgMusics.instance.getLoopManager().startTimer(p, regionConfig);
                    }
                }
            }
        }.runTaskLater(RpgMusics.instance, 1);

    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e){
        if (RpgMusics.instance.getLoopManager().tasks.containsKey(e.getPlayer().getUniqueId())){
            if (!RpgMusics.instance.getLoopManager().tasks.get(e.getPlayer().getUniqueId()).getBukkitTask().isCancelled()){
                RpgMusics.instance.getLoopManager().stopTimer(e.getPlayer());
            }
            RpgMusics.instance.getLoopManager().tasks.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        if (RpgMusics.instance.getLoopManager().tasks.containsKey(e.getPlayer().getUniqueId())){
            if (!RpgMusics.instance.getLoopManager().tasks.get(e.getPlayer().getUniqueId()).getBukkitTask().isCancelled()){
                RpgMusics.instance.getLoopManager().stopTimer(e.getPlayer());
            }
            RpgMusics.instance.getLoopManager().tasks.remove(e.getPlayer().getUniqueId());
        }
    }

    private RegionConfig getMusicRegion(Location loc){
        for (ProtectedRegion region : WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector(loc))){
            for (RegionConfig regionConfig : RpgMusics.instance.getRegionManager().regions){
                if (regionConfig.getRegion() == region){
                    return regionConfig;
                }
            }
        }
        return null;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        if (!e.getFrom().getWorld().getName().equalsIgnoreCase(e.getTo().getWorld().getName())) return;
        if (getMusicRegion(e.getTo()) == null){
            for (RegionConfig regionConfig : RpgMusics.instance.getRegionManager().regions){
                if (regionConfig.getRegionName().equalsIgnoreCase("__global__") && regionConfig.getRegionWorld().equalsIgnoreCase(e.getPlayer().getWorld().getName())){
                    tryPlayMusic(e.getPlayer(), regionConfig);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerChangedWorldEvent e){
        if (getMusicRegion(e.getPlayer().getLocation()) == null){
            for (RegionConfig regionConfig : RpgMusics.instance.getRegionManager().regions){
                if (regionConfig.getRegionName().equalsIgnoreCase("__global__") && regionConfig.getRegionWorld().equalsIgnoreCase(e.getPlayer().getWorld().getName())){
                    tryPlayMusic(e.getPlayer(), regionConfig);
                    break;
                }
            }
        }
    }

}
