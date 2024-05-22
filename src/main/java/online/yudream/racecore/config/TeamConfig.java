package online.yudream.racecore.config;

import lombok.Data;
import online.yudream.racecore.data.TeamData;
import online.yudream.racecore.entity.BaseTeam;
import online.yudream.racecore.utils.TeamUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

@Data
public class TeamConfig {
    public static YamlConfiguration index;
    private int maxPlayer;
    private int minPlayer;
    private boolean enableFileLoad;
    private List<BaseTeam> teams;

    public TeamConfig() {
        maxPlayer = index.getInt("number.max");
        minPlayer = index.getInt("number.min");
        enableFileLoad = index.getBoolean("loadFromFile");
        if (enableFileLoad) {
            teams = TeamUtils.loadTeamFromFile();
            for (BaseTeam team : teams) {
                TeamData.teams.put(team.getId(),team);
            }
        }
    }

}
