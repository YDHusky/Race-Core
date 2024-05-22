package online.yudream.racecore.data;

import online.yudream.racecore.entity.BaseTeam;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeamData {
    public static Scoreboard teamScoreboard;
    public static Map<Integer, BaseTeam> teams = new HashMap<>();
    public static Map<String, Integer> invites = new HashMap<>();
    public static Map<String, BukkitTask> tasks = new HashMap<>();
    public static Set<String> alreadyJoined = new HashSet<>();

    public static int getTeamIdByCaption(OfflinePlayer caption) {
        for (Map.Entry<Integer, BaseTeam> entry : teams.entrySet()) {
            BaseTeam team = entry.getValue();
            if (team.getCaptain().equals(caption)){
                return team.getId();
            }
        }
        return 0;
    }

}
