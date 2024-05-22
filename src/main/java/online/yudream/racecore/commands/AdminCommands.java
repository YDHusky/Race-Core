package online.yudream.racecore.commands;

import online.yudream.racecore.utils.FileUtils;
import online.yudream.racecore.utils.MapResetUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class AdminCommands implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            switch (strings[0]) {
                case "saveWorld":
                    if (strings.length == 2) {
                        String worldName = strings[1];
                        MapResetUtils.saveWorld(worldName);
                    } else {
                        sender.sendMessage(command.getUsage());
                    }
                    return false;
                case "resetWorld":
                    if (strings.length == 2) {
                        String worldName = strings[1];
                        MapResetUtils.resetWorld(worldName);
                    } else {
                        sender.sendMessage(command.getUsage());
                    }
                    return false;
                case "help":
                    sender.sendMessage(Objects.requireNonNull(FileUtils.readResourceFile("help.txt")));
                    return false;
            }
        } else {
            sender.sendMessage(command.getUsage());
        }
        return false;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("saveWorld", "resetWorld", "help");
    }
}
