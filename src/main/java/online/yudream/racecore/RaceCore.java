package online.yudream.racecore;

import online.yudream.racecore.commands.AdminCommands;
import online.yudream.racecore.commands.PlayerCommands;
import online.yudream.racecore.utils.StartUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public final class RaceCore extends JavaPlugin {

    public static RaceCore INSTANCE;

    @Override
    public void onEnable() {
        // 初始化接口
        INSTANCE = this;
        // 插件启动
        StartUtils.startPlugin();
        // 注册指令
        Objects.requireNonNull(Bukkit.getPluginCommand("RaceCoreAdmin")).setExecutor(new AdminCommands());
        Objects.requireNonNull(Bukkit.getPluginCommand("RaceCoreAdmin")).setTabCompleter(new AdminCommands());
        Objects.requireNonNull(Bukkit.getPluginCommand("RaceCore")).setExecutor(new PlayerCommands());
        Objects.requireNonNull(Bukkit.getPluginCommand("RaceCore")).setTabCompleter(new PlayerCommands());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
