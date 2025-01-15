package net.olokw.rpgmusics;

import com.google.common.reflect.ClassPath;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Listeners {
    public static void register() {
        try {
            ClassPath cp = ClassPath.from(Listeners.class.getClassLoader());

            for (ClassPath.ClassInfo classInfo : cp.getTopLevelClassesRecursive("net.olokw.rpgmusics.Events")) {
                Class<?> c = Class.forName(classInfo.getName());
                if (Listener.class.isAssignableFrom(c)) {
                    Listener listener = (Listener) c.getDeclaredConstructor().newInstance();
                    Bukkit.getServer().getPluginManager().registerEvents(listener, RpgMusics.instance);
                }
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
