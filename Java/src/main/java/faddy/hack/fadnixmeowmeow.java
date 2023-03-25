package faddy.hack;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;
import javax.swing.*;
import java.awt.event.*;

public class fadnixmeowmeow {

    public static void main(String[] args) {
        Path appDataRoamingDir = Paths.get(System.getProperty("user.home"), "AppData", "Roaming");
        Path desktopDir = Paths.get(System.getProperty("user.home"), "Desktop");
        Path oneDriveDesktopDir = Paths.get(System.getProperty("user.home"), "OneDrive", "Desktop");
        Path downloadsDir = Paths.get(System.getProperty("user.home"), "Downloads");
        Path[] jarFileDirs = new Path[] {
                appDataRoamingDir,
                desktopDir,
                oneDriveDesktopDir,
                downloadsDir
        };
        Path hacksFolder = Paths.get(System.getProperty("user.home"), "Desktop", "hacks", "fabric-forge mods");
        if (!Files.exists(hacksFolder)) {
            try {
                Files.createDirectories(hacksFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Pattern pattern = Pattern.compile("\"name\"\\s*:\\s*\"(.*?)\"", Pattern.CASE_INSENSITIVE);
        Path susFile = Paths.get(System.getProperty("user.home"), "Desktop", "hacks", "! Suspicious.txt");
        Path suspiciousFile = Paths.get(System.getProperty("user.home"), "Desktop", "hacks", "!!! Extremely Suspicious.txt");
        Path versionsFile = Paths.get(System.getProperty("user.home"), "Desktop", "hacks", "versions.txt");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(susFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
             PrintWriter suspiciousWriter = new PrintWriter(Files.newBufferedWriter(suspiciousFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
             PrintWriter versionsWriter = new PrintWriter(Files.newBufferedWriter(versionsFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            for (Path jarFileDir : jarFileDirs) {
                if (Files.isDirectory(jarFileDir)) {
                    searchForge(jarFileDir, hacksFolder, pattern, writer, suspiciousWriter);
                    searchJarFiles(jarFileDir, hacksFolder, pattern, writer, suspiciousWriter);
                }
            }
            Path versionsDir = Paths.get(System.getProperty("user.home"), "AppData", "Roaming", ".minecraft", "versions");
            if (Files.isDirectory(versionsDir)) {
                try (DirectoryStream<Path> versionsStream = Files.newDirectoryStream(versionsDir)) {
                    for (Path version : versionsStream) {
                        if (Files.isDirectory(version)) {
                            versionsWriter.println(version.getFileName().toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void searchJarFiles(Path directory, Path hacksFolder, Pattern pattern, PrintWriter writer, PrintWriter suspiciousWriter) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    searchJarFiles(path, hacksFolder, pattern, writer, suspiciousWriter);
                } else if (path.getFileName().toString().toLowerCase().endsWith(".jar")
                        && Files.size(path) <= 8 * 1024 * 1024) {
                    try (ZipFile zipFile = new ZipFile(path.toFile())) {
                        ZipEntry fabricModJsonEntry = zipFile.getEntry("fabric.mod.json");
                        if (fabricModJsonEntry != null) {
                            Path jarFile = hacksFolder.resolve(path.getFileName());
                            Files.copy(path, jarFile, StandardCopyOption.REPLACE_EXISTING);
                            try (Scanner scanner = new Scanner(zipFile.getInputStream(fabricModJsonEntry))) {
                                boolean shouldSkip = false;
                                while (scanner.hasNextLine()) {
                                    String line = scanner.nextLine();
                                    Matcher matcher = pattern.matcher(line);
                                    if (matcher.find()) {
                                        String nameFieldValue = matcher.group(1);
                                        if (nameFieldValue.toLowerCase().contains("fabric") || nameFieldValue.toLowerCase().contains("satin") || nameFieldValue.toLowerCase().contains("snakeyaml") || nameFieldValue.toLowerCase().contains("jcpp") || nameFieldValue.toLowerCase().contains("antlr4-runtime") || nameFieldValue.toLowerCase().contains("glsl-transformer") || nameFieldValue.toLowerCase().contains("core") || nameFieldValue.toLowerCase().contains("json") || nameFieldValue.toLowerCase().contains("toml")) {
                                            shouldSkip = true;
                                            break;
                                        } else if (nameFieldValue.toLowerCase().contains("meteor") || nameFieldValue.toLowerCase().contains("wurst") || nameFieldValue.toLowerCase().contains("hack") || nameFieldValue.toLowerCase().contains("aristois") || nameFieldValue.toLowerCase().contains("addon") || nameFieldValue.toLowerCase().contains("fdp") || nameFieldValue.toLowerCase().contains("nightware") || nameFieldValue.toLowerCase().contains("inertia")) {
                                            suspiciousWriter.println("MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.toString());
                                        } else { writer.println("MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.toString()); }
                                    }
                                }
                                if (shouldSkip) {
                                    continue;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

        private static void searchForge(Path directory, Path hacksFolder, Pattern pattern, PrintWriter writer, PrintWriter suspiciousWriter) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    searchForge(path, hacksFolder, pattern, writer, suspiciousWriter);
                } else if (path.getFileName().toString().toLowerCase().endsWith(".jar")
                        && Files.size(path) <= 8 * 1024 * 1024) {
                    try (ZipFile zipFile = new ZipFile(path.toFile())) {
                        ZipEntry mcmodentry = zipFile.getEntry("mcmod.info");
                        if (mcmodentry != null) {
                            Path jarFile = hacksFolder.resolve(path.getFileName());
                            Files.copy(path, jarFile, StandardCopyOption.REPLACE_EXISTING);
                            try (Scanner scanner = new Scanner(zipFile.getInputStream(mcmodentry))) {
                                boolean monkey = false;
                                while (scanner.hasNextLine()) {
                                    String line = scanner.nextLine();
                                    Matcher matcher = pattern.matcher(line);
                                    if (matcher.find()) {
                                        String nameFieldValue = matcher.group(1);
                                        if (nameFieldValue.toLowerCase().contains("fabric") || nameFieldValue.toLowerCase().contains("satin") || nameFieldValue.toLowerCase().contains("snakeyaml") || nameFieldValue.toLowerCase().contains("jcpp") || nameFieldValue.toLowerCase().contains("antlr4-runtime") || nameFieldValue.toLowerCase().contains("glsl-transformer") || nameFieldValue.toLowerCase().contains("core") || nameFieldValue.toLowerCase().contains("json") || nameFieldValue.toLowerCase().contains("toml")) {
                                            monkey = true;
                                            break;
                                        } else if (nameFieldValue.toLowerCase().contains("3arthh4ck") || nameFieldValue.toLowerCase().contains("impact") || nameFieldValue.toLowerCase().contains("meteor") || nameFieldValue.toLowerCase().contains("client") || nameFieldValue.toLowerCase().contains("wurst") || nameFieldValue.toLowerCase().contains("hack") || nameFieldValue.toLowerCase().contains("aristois") || nameFieldValue.toLowerCase().contains("addon") || nameFieldValue.toLowerCase().contains("fdp") || nameFieldValue.toLowerCase().contains("nightware") || nameFieldValue.toLowerCase().contains("inertia")) {
                                            suspiciousWriter.println("MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.toString());
                                        } else { writer.println("MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.toString()); }
                                    }
                                }
                                if (monkey) {
                                    continue;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}