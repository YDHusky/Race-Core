package online.yudream.racecore.commands;

import online.yudream.racecore.entity.BaseTeam;
import online.yudream.racecore.utils.FileUtils;
import online.yudream.racecore.utils.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerCommands implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            switch (strings[0]) {
                case "create":
                    if (strings.length == 2) {
                        if (commandSender instanceof Player) {
                            Player player = (Player) commandSender;
                            String teamName = strings[1];
                            TeamUtils.createTeam(player.getName(), teamName);
                            return true;
                        }
                    }
                case "accept":
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        TeamUtils.join(player.getName());
                        return true;
                    }
                case "leave":
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        TeamUtils.leaveTeam(player.getName());
                        return true;
                    }
                case "invite":
                    if (strings.length == 2) {
                        if (commandSender instanceof Player) {
                            Player player = (Player) commandSender;
                            String inviter = strings[1];
                            TeamUtils.invitePlayerJoin(player.getName(), inviter);
                            return true;
                        }
                    }
                case "cancel":
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        TeamUtils.cancelJoin(player.getName());
                        return true;
                    }
                case "info":
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        TeamUtils.printMembers(player.getName());
                        return true;
                    }
                case "kick":
                    if (commandSender instanceof Player) {
                        if (strings.length == 2) {
                            Player player = (Player) commandSender;
                            String p = strings[1];
                            TeamUtils.kickPlayer(player.getName(), p);
                            return true;
                        }
                    }
                case "help":
                    commandSender.sendMessage(Objects.requireNonNull(FileUtils.readResourceFile("admin-help.txt")));
            }

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        switch (strings.length) {
            case 1:
                list = List.of("create", "accept", "leave", "cancel", "invite", "info", "kick", "help");
                return list;
            case 2:
                switch (strings[0]) {
                    case "invite":
                        Player p = (Player) commandSender;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (!p.getUniqueId().equals(player.getUniqueId())) {
                                list.add(player.getName());
                            }
                        }
                        return list;
                    case "kick":
                        Player player = (Player) commandSender;
                        BaseTeam team = TeamUtils.getTeam(player.getName());
                        for (OfflinePlayer offlinePlayer : team.getMembers()) {
                            list.add(offlinePlayer.getName());
                        }
                        return list;
                }
        }
        return list;
    }
}
