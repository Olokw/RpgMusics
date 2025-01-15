package net.olokw.rpgmusics.Utils;

import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class LoopConfig {
    private BukkitTask bukkitTask;
    private List<MusicConfig> musicConfigList = new ArrayList<>();
    private String actualMusic;
    private int actualMusicNumber = -1;
    private Integer i = 0;

    public Integer getI() {
        return i;
    }

    public void addI() {
        i++;
    }

    public String getActualMusic() {
        return actualMusic;
    }

    public void setActualMusic(String actualMusic) {
        this.actualMusic = actualMusic;
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    public void setBukkitTask(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    public List<MusicConfig> getMusicConfigList() {
        return musicConfigList;
    }

    public void setMusicConfigList(List<MusicConfig> musicConfigList) {
        this.musicConfigList = musicConfigList;
    }

    public List<MusicConfig> getAvailableMusics(long worldTime) {
        List<MusicConfig> musics = new ArrayList<>();
        for (MusicConfig music : musicConfigList){
            if (music.checkIfCanPlayAtTime(worldTime)){
                musics.add(music);
            }
        }
        return musics;
    }

    public int getActualMusicNumber() {
        return actualMusicNumber;
    }

    public void setActualMusicNumber(int actualMusicNumber) {
        this.actualMusicNumber = actualMusicNumber;
    }

    public void setI(Integer i) {
        this.i = i;
    }
}
