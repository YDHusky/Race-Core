package online.yudream.racecore.utils;

import online.yudream.racecore.RaceCore;
import online.yudream.racecore.config.MainConfig;
import online.yudream.racecore.config.TeamConfig;
import online.yudream.racecore.config.WorldConfig;
import online.yudream.racecore.data.TeamData;
import org.bukkit.Bukkit;

public class ConfigUtils {
    public static void initConfig() {
        // 主配置
        RaceCore.INSTANCE.saveDefaultConfig();
        MainConfig.index = RaceCore.INSTANCE.getConfig();
        // 地图配置
        String worldConfigPath = "setting/worlds.yml";
        RaceCore.INSTANCE.saveResource(worldConfigPath, false);
        WorldConfig.index = FileUtils.getConfigFile(worldConfigPath);
        // 队伍配置
        String teamConfigPath = "setting/team.yml";
        RaceCore.INSTANCE.saveResource(teamConfigPath, false);
        TeamConfig.index = FileUtils.getConfigFile(teamConfigPath);
        TeamData.teamScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }


}
