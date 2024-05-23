package online.yudream.racecore.commands;

import online.yudream.racecore.config.WorldConfig;
import online.yudream.racecore.entity.RaceWorld;
import online.yudream.racecore.utils.FileUtils;
import online.yudream.racecore.utils.MapResetUtils;
import online.yudream.racecore.utils.TeamUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminCommands implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("racecore.admin")) {
            sender.sendMessage("§c你没有权限使用该指令");
            return false;
        }
        if (strings.length > 0) {
            switch (strings[0]) {
                case "saveWorld":
                    if (strings.length == 2) {
                        String worldName = strings[1];
                        MapResetUtils.saveWorld(worldName);
                    } else {
                        sender.sendMessage(command.getUsage());
                    }
                    return true;
                case "resetWorld":
                    if (strings.length == 2) {
                        String worldName = strings[1];
                        MapResetUtils.resetWorld(worldName);
                    } else {
                        sender.sendMessage(command.getUsage());
                    }
                    return true;
                case "team":
                    if (strings.length > 1) {
                        switch (strings[1]) {
                            case "random":
                                if (strings.length == 3) {
                                    int size = Integer.parseInt(strings[2]);
                                    TeamUtils.randomTeam(size);
                                    return true;
                                } else if (strings.length == 4) {
                                    int size = Integer.parseInt(strings[2]);
                                    String[] players = strings[3].split(",");
                                    TeamUtils.randomTeam(players,size);
                                    return true;
                                }
                        }
                    }
                case "help":
                    sender.sendMessage(Objects.requireNonNull(FileUtils.readResourceFile("admin-help.txt")));
                    return true;
            }
        }
        return false;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        if (sender.hasPermission("racecore.admin")) {
            switch (strings.length) {
                case 1:
                    list.add("saveWorld");
                    list.add("resetWorld");
                    list.add("help");
                    list.add("team");
                    return list;
                case 2:
                    if (strings[0].equals("saveWorld") || strings[0].equals("resetWorld")) {
                        WorldConfig worldConfig = new WorldConfig();
                        for (RaceWorld world : worldConfig.getWorlds()) {
                            list.add(world.getName());
                        }
                        return list;
                    }
                    if (strings[0].equals("team")){
                        list.add("random");
                    }
            }
        }
        return list;
    }
}
