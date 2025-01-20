package net.olokw.rpgmusics.Managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.olokw.rpgmusics.RpgMusics;
import net.olokw.rpgmusics.Utils.LoopConfig;
import net.olokw.rpgmusics.Utils.MusicConfig;
import net.olokw.rpgmusics.Utils.RegionConfig;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class LoopManager {
    public Map<UUID, LoopConfig> tasks;

    public LoopManager() {
        this.tasks = new HashMap<>();
    }

    public void startTimer(Player player, RegionConfig regionConfig) {
        List<MusicConfig> musicConfigList = regionConfig.getMusicConfigList();
        LoopConfig loopConfig = new LoopConfig();
        loopConfig.setMusicConfigList(musicConfigList);


        if (loopConfig.getAvailableMusics(player.getWorld().getTime()).isEmpty()){
            return;
        }

        int randomNumber = new Random().nextInt(0, loopConfig.getAvailableMusics(player.getWorld().getTime()).size());
        MusicConfig pickedMusicConfig = loopConfig.getAvailableMusics(player.getWorld().getTime()).get(randomNumber);

        justStartTimer(player, loopConfig, pickedMusicConfig, randomNumber);

        tasks.put(player.getUniqueId(), loopConfig);


    }

    public void justStartTimer(Player player, LoopConfig loopConfig, MusicConfig pickedMusicConfig, int actualNumber){
        final String music = pickedMusicConfig.getMusicName();
        final float volume = pickedMusicConfig.getVolume();
        final float pitch = pickedMusicConfig.getPitch();
        int loopTime = pickedMusicConfig.getLoopTime();
        loopConfig.setActualMusic(music);
        loopConfig.setActualMusicNumber(actualNumber);
        BukkitTask task = new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (loopConfig.getI() > 0){
                    boolean canContinue = false;
                    RegionConfig region = getAnyMusicRegion(player.getLocation());
                    if (region != null) canContinue = true;
                    int a = 0;
                    for (MusicConfig music : loopConfig.getMusicConfigList()){
                        if (!music.checkIfCanPlayAtTime(player.getWorld().getTime())){
                            a++;
                        }
                    }
                    if (a == loopConfig.getMusicConfigList().size()){
                        canContinue = false;
                    }
                    if (!canContinue){
                        tasks.remove(player.getUniqueId());
                        this.cancel();
                        return;
                    }
                }



                if (i > 0){
                    int randomNumber;

                    if (loopConfig.getAvailableMusics(player.getWorld().getTime()).size() > 1) {
                        int availableMusicsSize = loopConfig.getAvailableMusics(player.getWorld().getTime()).size();
                        int lastMusicNumber = loopConfig.getActualMusicNumber();

                        do {
                            randomNumber = new Random().nextInt(availableMusicsSize);
                        } while (randomNumber == lastMusicNumber);

                    } else {
                        randomNumber = 0;
                    }

                    MusicConfig newPickedMusicConfig = loopConfig.getAvailableMusics(player.getWorld().getTime()).get(randomNumber);
                    justStartTimer(player, loopConfig, newPickedMusicConfig, randomNumber);
                    this.cancel();
                    loopConfig.addI();
                    return;
                }else{
                    player.playSound(player.getLocation(), music, SoundCategory.RECORDS, volume, pitch);
                    i++;
                }

            }
        }.runTaskTimer(RpgMusics.instance, 0, loopTime);
        loopConfig.setBukkitTask(task);

    }

    public void stopTimer(Player player){
        if (tasks.get(player.getUniqueId()) != null) {
            tasks.get(player.getUniqueId()).getBukkitTask().cancel();
        }
    }

    private RegionConfig getAnyMusicRegion(Location loc){
        RegionConfig globalRegion = null;
        ApplicableRegionSet regionSet = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        if (regionSet.size() != 0){
            for (ProtectedRegion region : regionSet){
                for (RegionConfig regionConfig : RpgMusics.instance.getRegionManager().regions){
                    if (regionConfig.getRegionWorld().equalsIgnoreCase(loc.getWorld().getName())){
                        if (regionConfig.getRegionName().equalsIgnoreCase(region.getId())){
                            return regionConfig;
                        }
                        if (regionConfig.getRegionName().equalsIgnoreCase("__global__")){
                            globalRegion = regionConfig;
                        }
                    }
                }
            }
        } else {
            for (RegionConfig regionConfig : RpgMusics.instance.getRegionManager().regions){
                if (regionConfig.getRegionWorld().equalsIgnoreCase(loc.getWorld().getName())){
                    if (regionConfig.getRegionName().equalsIgnoreCase("__global__")){
                        return regionConfig;
                    }
                }
            }
        }
        return globalRegion;
    }

}