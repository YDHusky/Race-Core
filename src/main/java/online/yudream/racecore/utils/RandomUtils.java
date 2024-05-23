package online.yudream.racecore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;

import java.nio.Buffer;
import java.util.*;


public class RandomUtils {

    private static final Random RANDOM = new Random();
    private static final Set<Color> usedColors = new HashSet<>();

    public static Color generateRandomColor() {
        Color color;
        do {
            int red = RANDOM.nextInt(256);
            int green = RANDOM.nextInt(256);
            int blue = RANDOM.nextInt(256);
            color = Color.fromRGB(red, green, blue);
        } while (usedColors.contains(color) || !isColorDistinct(color));
        usedColors.add(color);
        return color;
    }

    private static boolean isColorDistinct(Color color) {
        for (Color usedColor : usedColors) {
            if (colorDistance(color, usedColor) < 100) { // 调整阈值以确保颜色分散
                return false;
            }
        }
        return true;
    }

    private static double colorDistance(Color c1, Color c2) {
        int redDiff = c1.getRed() - c2.getRed();
        int greenDiff = c1.getGreen() - c2.getGreen();
        int blueDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }

    private static final ChatColor[] CHAT_COLORS = ChatColor.values();

    public static ChatColor getRandomChatColor() {
        return CHAT_COLORS[RANDOM.nextInt(CHAT_COLORS.length)];
    }

    public static List<List<OfflinePlayer>> randomPlayer(List<String> players, int size) {
        List<List<OfflinePlayer>> playerGroups = new ArrayList<>();
        int total = players.size();

        int groupNum = total / size + (total % size == 0 ? 0 : 1);
        total--;
        for (int i=0;i<groupNum;i++){
            List<OfflinePlayer> playerGroup = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                if (total<0){
                    break;
                }else {
                    int index;
                    if (total>0){
                        index = RANDOM.nextInt(total--);
                    }else {
                        index = 0;
                    }
                    playerGroup.add(Bukkit.getOfflinePlayer(players.get(index)));
                    players.remove(index);
                }
            }
            playerGroups.add(playerGroup);
        }
        return playerGroups;
    }

}
