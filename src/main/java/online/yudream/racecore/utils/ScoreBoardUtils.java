package online.yudream.racecore.utils;

import online.yudream.racecore.data.TeamData;
import online.yudream.racecore.entity.BaseTeam;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Map;

public class ScoreBoardUtils {
    public static void initScoreBoard() {
        // 计分板初始化
        TeamData.teamScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = TeamData.teamScoreboard.registerNewObjective("team","dummy","§9=======§c比赛信息§9=======");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        initScore();
    }

    public static void initScore(){
        Objective objective = TeamData.teamScoreboard.getObjective("team");
        for (Map.Entry<Integer,BaseTeam> entry:TeamData.teams.entrySet()){
            BaseTeam baseTeam = entry.getValue();
            Score score = objective.getScore(baseTeam.getTeam().getColor()+baseTeam.getDisplayName());
            score.setScore(baseTeam.getScore());
        }
        for (String player:TeamData.alreadyJoined){
            Bukkit.getPlayer(player).setScoreboard(TeamData.teamScoreboard);
        }
    }
}
