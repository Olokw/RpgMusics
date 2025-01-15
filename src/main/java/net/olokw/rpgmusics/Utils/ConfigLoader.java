package net.olokw.rpgmusics.Utils;

import net.olokw.rpgmusics.RpgMusics;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigLoader {
    public void load(){

        RpgMusics.instance.getLoopManager().tasks.clear();
        RpgMusics.instance.getRegionManager().clear();

        File file = new File(RpgMusics.instance.getDataFolder(), "regions.yml");
        if(file.exists()){
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {

                String regionName = config.getString(key + ".region-name");

                String regionWorld = config.getString(key + ".region-world");

                RegionConfig regionConfig = new RegionConfig();
                regionConfig.setRegionName(regionName);
                regionConfig.setRegionWorld(regionWorld);

                String music = config.getString(key + ".music");

                int loopTime = config.getInt(key + ".loop-time");

                float volume = Float.parseFloat(config.getString(key + ".volume"));

                float pitch = Float.parseFloat(config.getString(key + ".pitch"));

                MusicConfig musicConfig = new MusicConfig(music, loopTime, volume, pitch);

                if (config.getString(key + ".time").equalsIgnoreCase("none")){
                    musicConfig.setTimeDisabled(true);
                }else{
                    String[] time = config.getString(key + ".time").split("-");
                    int time1 = Integer.parseInt(time[0]);
                    int time2 = Integer.parseInt(time[1]);
                    musicConfig.setTime(time1, time2);
                }



                if (RpgMusics.instance.getRegionManager().regions.containsKey(regionConfig.getRegion())){
                    RpgMusics.instance.getRegionManager().regions.get(regionConfig.getRegion()).addMusicConfig(musicConfig);
                }else{
                    regionConfig.addMusicConfig(musicConfig);
                    RpgMusics.instance.getRegionManager().add(regionConfig.getRegion(), regionConfig);
                }
            }
        }else{

            try {
                RpgMusics.instance.getDataFolder().mkdir();
                Files.copy(RpgMusics.instance.getResource("regions.yml"), file.getAbsoluteFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
