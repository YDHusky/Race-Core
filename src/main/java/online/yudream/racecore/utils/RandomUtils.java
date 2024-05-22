package online.yudream.racecore.utils;

import org.bukkit.Color;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


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
}
