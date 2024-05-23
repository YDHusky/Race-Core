package online.yudream.racecore.utils;

import online.yudream.racecore.RaceCore;

/**
 * 启动工具
 * @author SiberianHusky
 * @date 2024-5-21
 */
public class StartUtils {
    public static void startPlugin() {
        RaceCore.INSTANCE.getLogger().info("\n" + FileUtils.readResourceFile("start.txt") + "\n");
        // 初始化配置
        ConfigUtils.initConfig();
        // 初始化世界
        MapResetUtils.initWorld();
        // 初始化计分板
        ScoreBoardUtils.initScoreBoard();
        // 初始化队伍
        DataUtils.initTeamsData();
    }
}
