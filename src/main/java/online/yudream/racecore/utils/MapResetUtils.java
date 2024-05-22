package online.yudream.racecore.utils;


import online.yudream.racecore.RaceCore;
import online.yudream.racecore.config.WorldConfig;
import online.yudream.racecore.entity.RaceWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MapResetUtils {
    /**
     * 保存世界
     *
     * @param worldName 世界名字
     */
    public static void saveWorld(String worldName) {
        if (unloadWorld(worldName) > 0) {
            return;
        }
        File worldFile = new File(Bukkit.getWorldContainer(), worldName);

        File mapDir = new File(RaceCore.INSTANCE.getDataFolder(), "map");
        if (!mapDir.exists()) {
            mapDir.mkdir();
        }
        File saveMap = new File(mapDir, worldName + ".zip");
        try {
            FileUtils.zipDirectory(worldFile.toPath(), saveMap.toPath());
            RaceCore.INSTANCE.getLogger().info("保存" + worldName + "成功！");
            loadWorld(worldName);
        } catch (IOException e) {
            RaceCore.INSTANCE.getLogger().warning(e.getMessage());
        }
    }

    public static void initWorld() {
        WorldConfig worldConfig = new WorldConfig();
        for (RaceWorld world : worldConfig.getWorlds()) {
            File worldFile = new File(Bukkit.getWorldContainer(), world.getName());
            File savedWorldFile = new File(RaceCore.INSTANCE.getDataFolder(), "map/" + world.getName() + ".zip");

            if (!worldFile.exists()) {
                if (savedWorldFile.exists()) {
                    File worldDir = new File(Bukkit.getWorldContainer(), world.getName());
                    try {
                        FileUtils.unzipDirectory(savedWorldFile.toPath(), worldDir.toPath());
                        loadWorld(world.getName());
                    } catch (IOException e) {
                        RaceCore.INSTANCE.getLogger().warning(e.getMessage());
                    }
                } else {
                    createWorld(world.getName());
                }
            } else {
                loadWorld(world.getName());
            }
        }
    }

    public static void loadWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            WorldCreator worldCreator = new WorldCreator(worldName);
            world = Bukkit.createWorld(worldCreator);
            if (world != null) {
                RaceCore.INSTANCE.getLogger().info("成功加载现有世界: " + worldName);
            } else {
                RaceCore.INSTANCE.getLogger().warning("加载世界失败: " + worldName);
            }
        } else {
            RaceCore.INSTANCE.getLogger().info("世界已经加载: " + worldName);
        }
    }

    public static int createWorld(String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(World.Environment.NORMAL); // 设置世界环境，默认是普通
        worldCreator.generateStructures(true); // 生成结构
        World newWorld = Bukkit.createWorld(worldCreator);
        if (newWorld != null) {
            RaceCore.INSTANCE.getLogger().info("成功创建新世界: " + worldName);
            return 0;
        } else {
            RaceCore.INSTANCE.getLogger().warning("创建世界失败: " + worldName);
            return 1;
        }
    }

    /**
     * 重置世界
     *
     * @param worldName 世界名字
     */
    public static void resetWorld(String worldName) {
        File zipFile = new File(RaceCore.INSTANCE.getDataFolder(), "map/" + worldName + ".zip");
        if (!zipFile.exists()) {
            RaceCore.INSTANCE.getLogger().warning(worldName + "备份不存在！");
            return;
        }
        if (unloadWorld(worldName) > 0) {
            return;
        }
        File worldDir = new File(Bukkit.getWorldContainer(), worldName);
        FileUtils.deleteDirectory(worldDir.toPath());
        try {
            FileUtils.unzipDirectory(zipFile.toPath(), worldDir.toPath());
            loadWorld(worldName);
        } catch (IOException e) {
            RaceCore.INSTANCE.getLogger().warning(e.getMessage());
        }
    }

    /**
     * 卸载世界
     *
     * @param worldName 世界名字
     * @return 0成功，大于0失败
     */
    private static int unloadWorld(String worldName) {
        if (worldName.equalsIgnoreCase("world")) {
            RaceCore.INSTANCE.getLogger().warning("不能卸载world主世界，请进行手动操作！");
            return 1;
        }
        try {
            World savedWorld = Bukkit.getWorld(worldName);
            World mainWorld = Bukkit.getWorld("world");
            List<Player> playerList = savedWorld.getPlayers();
            Location location = mainWorld.getSpawnLocation();
            // 将玩家传送出该世界
            for (Player player : playerList) {
                player.teleport(location);
            }
        }catch (NullPointerException e){
            RaceCore.INSTANCE.getLogger().warning(e.getMessage());
        }
        // 卸载世界
        Bukkit.unloadWorld(worldName, true);
        return 0;
    }
}