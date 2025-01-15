package net.olokw.rpgmusics.Utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class RegionConfig {
    private String regionName;
    private String regionWorld;
    private List<MusicConfig> music = new ArrayList<>();

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setRegionWorld(String regionWorld) {
        this.regionWorld = regionWorld;
    }

    public ProtectedRegion getRegion(){
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld(regionWorld))).getRegion(regionName);
    }

    public String getRegionName() {
        return regionName;
    }

    public String getRegionWorld() {
        return regionWorld;
    }

    public List<MusicConfig> getMusicConfigList() {
        return music;
    }

    public void addMusicConfig(MusicConfig music) {
        this.music.add(music);
    }

    public int getAvailableMusicsSize(long worldTime) {
        int musics = 0;
        for (MusicConfig music : music){
            if (music.checkIfCanPlayAtTime(worldTime)){
                musics++;
            }
        }
        return musics;
    }
}
