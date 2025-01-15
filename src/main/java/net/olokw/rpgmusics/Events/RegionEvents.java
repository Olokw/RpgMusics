package net.olokw.rpgmusics.Events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.netzkronehd.wgregionevents.events.RegionEnterEvent;
import net.olokw.rpgmusics.RpgMusics;
import net.olokw.rpgmusics.Utils.LoopConfig;
import net.olokw.rpgmusics.Utils.RegionConfig;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class RegionEvents implements Listener {
    @EventHandler
    public void onRegionEnter(final RegionEnterEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(e.getPlayer().getWorld())) == null) return;

                int i = 0;
                for (ProtectedRegion region : WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(e.getPlayer().getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector(e.getPlayer().getLocation()))) {
                    if (i != 0) {
                        return;
                    }

                    if (RpgMusics.instance.getRegionManager().regions.containsKey(region)) {
                        RegionConfig regionConfig = RpgMusics.instance.getRegionManager().regions.get(region);

                        if (RpgMusics.instance.getLoopManager().tasks.containsKey(e.getPlayer().getUniqueId())) {
                            LoopConfig currentLoopConfig = RpgMusics.instance.getLoopManager().tasks.get(e.getPlayer().getUniqueId());

                            if (!new HashSet<>(currentLoopConfig.getMusicConfigList()).containsAll(regionConfig.getMusicConfigList())
                                    && regionConfig.getAvailableMusicsSize(e.getPlayer().getWorld().getTime()) > 0) {

                                if (!currentLoopConfig.getActualMusic().equals(regionConfig.getMusicConfigList().get(0).getMusicName())) {
                                    e.getPlayer().stopSound(currentLoopConfig.getActualMusic(), SoundCategory.RECORDS);
                                    RpgMusics.instance.getLoopManager().stopTimer(e.getPlayer());
                                    RpgMusics.instance.getLoopManager().tasks.remove(e.getPlayer().getUniqueId());
                                }
                            }
                        }

                        if (!RpgMusics.instance.getLoopManager().tasks.containsKey(e.getPlayer().getUniqueId())) {
                            String currentMusic = RpgMusics.instance.getLoopManager().tasks.containsKey(e.getPlayer().getUniqueId())
                                    ? RpgMusics.instance.getLoopManager().tasks.get(e.getPlayer().getUniqueId()).getActualMusic()
                                    : null;

                            String regionMusic = regionConfig.getMusicConfigList().get(0).getMusicName();

                            if (!regionMusic.equals(currentMusic)) {
                                RpgMusics.instance.getLoopManager().startTimer(e.getPlayer(), regionConfig);
                                i++;
                            }
                        }
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e){
        if (e.getFrom().getWorld().equals(e.getTo().getWorld())){
            e.getPlayer().stopAllSounds();
        }
    }

}
