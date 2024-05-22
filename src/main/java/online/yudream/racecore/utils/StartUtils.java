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
        ConfigUtils.initConfig();
        MapResetUtils.initWorld();
    }
}
