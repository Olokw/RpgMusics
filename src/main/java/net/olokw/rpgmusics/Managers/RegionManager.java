package net.olokw.rpgmusics.Managers;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.olokw.rpgmusics.Utils.RegionConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegionManager {

    public Set<RegionConfig> regions;

    public RegionManager() {
        this.regions = new HashSet<>();
    }

    public void clear() {
        regions.clear();
    }
    public void add(RegionConfig regionConfig) {
        regions.add(regionConfig);
    }

}
