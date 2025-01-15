package net.olokw.rpgmusics.Utils;

public class MusicConfig {
    private int loopTime;
    private float volume;
    private float pitch;
    private String musicName;

    private boolean timeDisabled = false;
    private int time1;
    private int time2;


    public MusicConfig(String musicName, int loopTime, float volume, float pitch) {
        this.loopTime = loopTime;
        this.volume = volume;
        this.pitch = pitch;
        this.musicName = musicName;
    }

    public void setTimeDisabled(boolean timeDisabled) {
        this.timeDisabled = timeDisabled;
    }

    public void setTime(int time1, int time2) {
        this.time1 = time1;
        this.time2 = time2;
    }

    public boolean isTimeDisabled() {
        return timeDisabled;
    }

    public int getTime1() {
        return time1;
    }

    public int getTime2() {
        return time2;
    }

    public int getLoopTime() {
        return loopTime;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public String getMusicName() {
        return musicName;
    }

    public boolean checkIfCanPlayAtTime(long worldTime){
        if (timeDisabled){
            return true;
        }else{
            return (worldTime > time1 && worldTime  < time2);
        }

    }
}
