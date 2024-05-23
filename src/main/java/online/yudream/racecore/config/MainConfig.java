package online.yudream.racecore.config;


import lombok.Data;
import org.bukkit.configuration.Configuration;

import java.util.List;

@Data
public class MainConfig {
    public static Configuration index;
    private String version;
    private String raceWorldName;
    private int minStartPlayer;


    public MainConfig() {
        version = getSting("version");
        raceWorldName = getSting("raceMap.name");
        minStartPlayer = getInt("minStartPlayer");
    }

    public static String getSting(String key) {
        return index.getString(key);
    }

    public static List<String> getStringList(String key) {
        return index.getStringList(key);
    }

    public static boolean getBoolean(String key) {
        return index.getBoolean(key);
    }
    public static int getInt(String key){
        return index.getInt(key);
    }
}
