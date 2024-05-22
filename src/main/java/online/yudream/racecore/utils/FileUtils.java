package online.yudream.racecore.utils;

import online.yudream.racecore.RaceCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具
 *
 * @author SiberianHusky
 * @date 2024-5-21
 */
public class FileUtils {
    /**
     * 读取资源文件中内容
     *
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readResourceFile(String fileName) {
        InputStream inputStream = RaceCore.INSTANCE.getResource(fileName);
        if (inputStream == null) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 压缩
     *
     * @param source 打包文件
     * @param target 打包目标
     * @throws IOException
     */
    public static void zipDirectory(Path source, Path target) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(target.toFile().toPath()))) {
            Files.walk(source).forEach(path -> {
                ZipEntry zipEntry = new ZipEntry(source.relativize(path).toString());
                try {
                    if (Files.isDirectory(path)) {
                        zipEntry = new ZipEntry(source.relativize(path) + "/");
                        zos.putNextEntry(zipEntry);
                        zos.closeEntry();
                    } else {
                        zos.putNextEntry(zipEntry);
                        Files.copy(path, zos);
                        zos.closeEntry();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to zip directory", e);
                }
            });
        }
    }

    /**
     * 解压缩
     *
     * @param source zip文件
     * @param target 解压目标
     * @throws IOException
     */
    public static void unzipDirectory(Path source, Path target) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                Path newPath = target.resolve(zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
    }

    public static YamlConfiguration getConfigFile(String path){
        File file = new File(RaceCore.INSTANCE.getDataFolder(), path);
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 删除文件夹
     *
     * @param path 文件夹地址
     */
    public static void deleteDirectory(Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to delete directory");
            e.printStackTrace();
        }
    }
}
