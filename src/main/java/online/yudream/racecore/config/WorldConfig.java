package online.yudream.racecore.config;

import lombok.Data;
import online.yudream.racecore.entity.RaceWorld;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class WorldConfig {
    public static YamlConfiguration index;

    private List<RaceWorld> worlds;

    public WorldConfig() {
        worlds = new ArrayList<>();
        Set<String> worldNames = index.getKeys(false);
        for (String worldName : worldNames) {
            worlds.add(RaceWorld.builder()
                    .name(worldName)
                    .desc(index.getString(worldName + ".desc"))
                    .build());
        }
    }
}
