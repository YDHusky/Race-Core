package online.yudream.racecore.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static String replaceColor(String text) {
        return text.replace("&", "§");
    }

    public static List<String> replaceColor(List<String> textList) {
        List<String> newTextList = new ArrayList<String>();
        for (String text : textList) {
            newTextList.add(replaceColor(text));
        }
        return newTextList;
    }
}
