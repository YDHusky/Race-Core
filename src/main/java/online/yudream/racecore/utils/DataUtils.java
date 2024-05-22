package online.yudream.racecore.utils;

import online.yudream.racecore.RaceCore;
import online.yudream.racecore.config.TeamConfig;

public class DataUtils {
    public static void initTeamsData(){
        // 检测是否开启从文件读取
        TeamConfig tc = new TeamConfig();
        // 初始化队伍配置
        if (tc.isEnableFileLoad()){
            String teamsDataPath = "data/teams.yml";
            RaceCore.INSTANCE.saveResource(teamsDataPath,false);
        }
    }
}
