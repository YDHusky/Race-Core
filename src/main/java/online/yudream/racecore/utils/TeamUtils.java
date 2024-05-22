package online.yudream.racecore.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import online.yudream.racecore.RaceCore;
import online.yudream.racecore.data.TeamData;
import online.yudream.racecore.entity.BaseTeam;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TeamUtils {

    public static void createTeam(String caption, String name) {
        if (TeamData.alreadyJoined.contains(caption)) {
            Bukkit.getPlayer(caption).sendMessage("§c你已经加入了一个队伍！");
        } else {
            BaseTeam team = BaseTeam.builder()
                    .captain(Bukkit.getOfflinePlayer(caption))
                    .displayName(name)
                    .id(++BaseTeam.teamNumber)
                    .color(RandomUtils.generateRandomColor())
                    .build();
            List<OfflinePlayer> members = new ArrayList<>();
            Team team1 = TeamData.teamScoreboard.registerNewTeam("team" + team.getId());
            team1.addEntry(caption);
            members.add(Bukkit.getOfflinePlayer(caption));
            team.setMembers(members);
            team.setTeam(team1);
            TeamData.alreadyJoined.add(caption);
            TeamData.teams.put(BaseTeam.teamNumber, team);
            Bukkit.getPlayer(caption).sendMessage("§9创建成功！");
        }
    }

    /**
     * 退出队伍
     *
     * @param player 玩家
     */
    public static void leaveTeam(String player) {
        if (TeamData.alreadyJoined.contains(player)) {
            for (Map.Entry<Integer, BaseTeam> entry : TeamData.teams.entrySet()) {
                List<OfflinePlayer> members = entry.getValue().getMembers();
                for (OfflinePlayer p : members) {
                    if (p == Bukkit.getOfflinePlayer(player)) {

                        BaseTeam team = TeamData.teams.get(entry.getKey());

                        members.remove(Bukkit.getOfflinePlayer(player));

                        Team team1 = team.getTeam();
                        team1.removeEntry(player);
                        team.setTeam(team1);
                        team.setMembers(members);
                        TeamData.alreadyJoined.remove(player);
                        // 当退出玩家为队长时候删除队伍
                        if (Objects.equals(team.getCaptain().getName(), player)){
                            team.getTeam().unregister();
                            TeamData.teams.remove(team.getId());
                        }
                        Bukkit.getPlayer(player).sendMessage("§c你退出了当前团队！");
                        return;
                    }
                }
            }
        }
    }

    /**
     * 邀请加入团队
     *
     * @param caption 队长
     * @param player  玩家
     */
    public static void invitePlayerJoin(String caption, String player) {
        int teamId = TeamData.getTeamIdByCaption(Bukkit.getOfflinePlayer(caption));
        if (teamId == 0) {
            Objects.requireNonNull(Bukkit.getPlayer(caption)).sendMessage("§c你不是队长！");
        }
        if (TeamData.alreadyJoined.contains(player)) {
            Objects.requireNonNull(Bukkit.getPlayer(caption)).sendMessage("§c该玩家已经了一个团队，你无法邀请他加入你！");
        } else {

            TeamData.invites.put(player, teamId);
            // 向被邀请玩家发送
            TextComponent message = Component.text("§9点击加入该队伍！").clickEvent(ClickEvent.runCommand("/rc help"));
            Objects.requireNonNull(Bukkit.getPlayer(player)).sendMessage(message);
            // 向队长发送
            TextComponent msg = Component.text("§9已向§c" + player + "§9发送邀请!").append(Component.text(("§c点击取消!")).clickEvent(ClickEvent.runCommand("/rc help")));
            Objects.requireNonNull(Bukkit.getPlayer(caption)).sendMessage(msg);
            //创建120s定时任务 120s超时取消邀请
            TeamData.tasks.put(player, new BukkitRunnable() {
                @Override
                public void run() {
                    Objects.requireNonNull(Bukkit.getPlayer(player)).sendMessage("§c邀请已经过时！");
                    Objects.requireNonNull(Bukkit.getPlayer(caption)).sendMessage("§c邀请已经过时！");
                    TeamData.invites.remove(player);
                    TeamData.tasks.remove(player);
                    this.cancel();
                }
            }.runTaskTimerAsynchronously(RaceCore.getPlugin(RaceCore.class), 20 * 120, 0));
        }
    }

    /**
     * 拒绝加入
     *
     * @param player 玩家
     */
    public static void cancelJoin(String player) {
        Integer teamId = TeamData.invites.get(player);
        if (teamId != null) {
            BaseTeam team = TeamData.teams.get(teamId);
            TeamData.invites.remove(player);
            TeamData.tasks.remove(player);
            Objects.requireNonNull(Bukkit.getPlayer(player)).sendMessage("§c拒绝了加入该团队！");
            Objects.requireNonNull(team.getCaptain().getPlayer()).sendMessage("§9" + player + "§c拒绝了你的邀请！");
        } else {
            Objects.requireNonNull(Bukkit.getPlayer(player)).sendMessage("§c你没有待处理的邀请！");
        }
    }

    /**
     * 同意加入
     *
     * @param player 玩家
     */
    public static void join(String player) {
        Integer teamId = TeamData.invites.get(player);
        if (teamId != null) {
            BaseTeam team = TeamData.teams.get(teamId);
            team.getMembers().add(Bukkit.getOfflinePlayer(player));

            Team team1 = team.getTeam();
            team1.addEntry(player);
            team.setTeam(team1);

            Objects.requireNonNull(team.getCaptain().getPlayer()).sendMessage("§9玩家§7" + player + "§9加入了你的队伍！");
            Objects.requireNonNull(Bukkit.getPlayer(player)).sendMessage("§9你加入了§7" + team.getCaptain().getName() + "§9的队伍！");

            TeamData.teams.put(teamId, team);
            TeamData.invites.remove(player);
            TeamData.tasks.remove(player);
            TeamData.alreadyJoined.add(player);
        }else{
            Objects.requireNonNull(Bukkit.getPlayer(player)).sendMessage("§c邀请你的队伍不存在！");
        }
    }

    /**
     * 从文件加载队伍
     *
     * @return 队伍
     */
    public static List<BaseTeam> loadTeamFromFile() {
        YamlConfiguration teamConfig = FileUtils.getConfigFile("data/teams.yml");
        List<BaseTeam> teams = new ArrayList<>();
        int teamNum = teamConfig.getKeys(false).size();
        for (int i = 1; i <= teamNum; i++) {
            String teamKey = "team" + i;
            BaseTeam baseTeam = BaseTeam.builder().
                    id(i)
                    .displayName(teamConfig.getString(teamKey + ".name"))
                    .color(teamConfig.getColor(teamKey + ".color"))
                    .build();
            // 计分板队伍注册
            Team team = TeamData.teamScoreboard.registerNewTeam(teamKey);
            team.setDisplayName(teamConfig.getString(teamKey + ".name"));
            team.setAllowFriendlyFire(false);
            // 对于自己的队伍开启防碰撞体积, 而对其他队伍开启体积碰撞
            // 这里的FOR_OWN_TEAM表示的意思是只对本队 关闭
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OWN_TEAM);

            List<String> players = teamConfig.getStringList(teamKey + ".players");
            baseTeam.setCaptain(Bukkit.getOfflinePlayer(players.get(0)));
            List<OfflinePlayer> members = new ArrayList<>();
            for (String player : players) {
                TeamData.alreadyJoined.add(player);
                team.addEntry(player);
                members.add(Bukkit.getOfflinePlayer(player));
            }
            baseTeam.setMembers(members);
            baseTeam.setTeam(team);
            teams.add(baseTeam);
        }
        return teams;
    }
}
