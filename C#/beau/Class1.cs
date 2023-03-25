using System;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using System.Collections.Generic;
using System.IO.Compression;

namespace Faddy.Meow
{
    class Program
    {
        static void Main(string[] args)
        {
            var appDataRoamingDir = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), "Roaming");
            var desktopDir = Environment.GetFolderPath(Environment.SpecialFolder.Desktop);
            var oneDriveDesktopDir = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.UserProfile), "OneDrive", "Desktop");
            var downloadsDir = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile) + "\\Downloads";
            var jarFileDirs = new string[] { appDataRoamingDir, desktopDir, oneDriveDesktopDir, downloadsDir };
            var hacksFolder = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.Desktop), "hacks", "fabric mods");
            if (!Directory.Exists(hacksFolder))
            {
                try
                {
                    Directory.CreateDirectory(hacksFolder);
                }
                catch (IOException e)
                {
                    Console.WriteLine(e.Message);
                }
            }
            var pattern = new Regex("\"name\"\\s*:\\s*\"(.*?)\"", RegexOptions.IgnoreCase);
            var susFile = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.Desktop), "hacks", "! Suspicious.txt");
            var suspiciousFile = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.Desktop), "hacks", "!!! Extremely Suspicious.txt");
            var versionsFile = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.Desktop), "hacks", "versions.txt");
            using (var writer = new StreamWriter(susFile, false))
            using (var suspiciousWriter = new StreamWriter(suspiciousFile, false))
            using (var versionsWriter = new StreamWriter(versionsFile, false))
            {
                foreach (var jarFileDir in jarFileDirs)
                {
                    if (Directory.Exists(jarFileDir))
                    {
                        SearchForge(jarFileDir, hacksFolder, pattern, writer, suspiciousWriter);
                        SearchJarFiles(jarFileDir, hacksFolder, pattern, writer, suspiciousWriter);
                    }
                }
                var versionsDir = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), ".minecraft", "versions");
                if (Directory.Exists(versionsDir))
                {
                    foreach (var version in Directory.EnumerateDirectories(versionsDir))
                    {
                        versionsWriter.WriteLine(Path.GetFileName(version));
                    }
                }
            }
        }

        private static void SearchJarFiles(string directory, string hacksFolder, Regex pattern, StreamWriter writer, StreamWriter suspiciousWriter)
        {
            foreach (var path in Directory.EnumerateFileSystemEntries(directory))
            {
                if (Directory.Exists(path))
                {
                    SearchJarFiles(path, hacksFolder, pattern, writer, suspiciousWriter);
                }
                else if (path.ToLower().EndsWith(".jar") && new FileInfo(path).Length <= 8 * 1024 * 1024)
                {
                    try
                    {
                        using (var zipFile = ZipFile.OpenRead(path))
                        {
                            var fabricModJsonEntry = zipFile.GetEntry("fabric.mod.json");
                            if (fabricModJsonEntry != null)
                            {
                                var jarFile = Path.Combine(hacksFolder, Path.GetFileName(path));
                                File.Copy(path, jarFile, true);
                                using (var scanner = new StreamReader(fabricModJsonEntry.Open()))
                                {
                                    bool shouldSkip = false;
                                    while (!scanner.EndOfStream)
                                    {
                                        var line = scanner.ReadLine();
                                        var matcher = pattern.Match(line);
                                        if (matcher.Success)
                                        {
                                            var nameFieldValue = matcher.Groups[1].Value;
                                            if (nameFieldValue.ToLower().Contains("fabric") || nameFieldValue.ToLower().Contains("satin") || nameFieldValue.ToLower().Contains("snakeyaml") || nameFieldValue.ToLower().Contains("jcpp") || nameFieldValue.ToLower().Contains("antlr4-runtime") || nameFieldValue.ToLower().Contains("glsl-transformer") || nameFieldValue.ToLower().Contains("core") || nameFieldValue.ToLower().Contains("json") || nameFieldValue.ToLower().Contains("toml"))
                                            {
                                                shouldSkip = true;
                                                break;
                                            }
                                            else if (nameFieldValue.ToLower().Contains("meteor") || nameFieldValue.ToLower().Contains("wurst") || nameFieldValue.ToLower().Contains("hack") || nameFieldValue.ToLower().Contains("aristois") || nameFieldValue.ToLower().Contains("addon") || nameFieldValue.ToLower().Contains("fdp") || nameFieldValue.ToLower().Contains("nightware") || nameFieldValue.ToLower().Contains("inertia"))
                                            {
                                                suspiciousWriter.WriteLine("MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.ToString());
                                            }
                                            else { writer.WriteLine("MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.ToString()); }
                                        }
                                    }
                                    if (shouldSkip) continue;
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        Console.WriteLine(e.Message);
                    }
                }
            }
        }

        private static void SearchForge(string directory, string hacksFolder, Regex pattern, StreamWriter writer, StreamWriter suspiciousWriter)
        {
            foreach (var path in Directory.EnumerateFileSystemEntries(directory))
            {
                if (Directory.Exists(path))
                {
                    SearchForge(path, hacksFolder, pattern, writer, suspiciousWriter);
                }
                else if (path.ToLower().EndsWith(".jar") && new FileInfo(path).Length <= 8 * 1024 * 1024)
                {
                    try
                    {
                        using (var zipFile = ZipFile.OpenRead(path))
                        {
                            var forgeModJsonEntry = zipFile.GetEntry("mcmod.info");
                            if (forgeModJsonEntry != null)
                            {
                                var jarFile = Path.Combine(hacksFolder, Path.GetFileName(path));
                                File.Copy(path, jarFile, true);
                                using (var scanner = new StreamReader(forgeModJsonEntry.Open()))
                                {
                                    bool monkey = false;
                                    while (!scanner.EndOfStream)
                                    {
                                        var line = scanner.ReadLine();
                                        var matcher = pattern.Match(line);
                                        if (matcher.Success)
                                        {
                                            var nameFieldValue = matcher.Groups[1].Value;
                                            if (nameFieldValue.ToLower().Contains("fabric") || nameFieldValue.ToLower().Contains("satin") || nameFieldValue.ToLower().Contains("snakeyaml") || nameFieldValue.ToLower().Contains("jcpp") || nameFieldValue.ToLower().Contains("antlr4-runtime") || nameFieldValue.ToLower().Contains("glsl-transformer") || nameFieldValue.ToLower().Contains("core") || nameFieldValue.ToLower().Contains("json") || nameFieldValue.ToLower().Contains("toml"))
                                            {
                                                monkey = true;
                                                break;
                                            }
                                            else if (nameFieldValue.ToLower().Contains("liquidbounce") || nameFieldValue.ToLower().Contains("3arthh4ck") || nameFieldValue.ToLower().Contains("impact") || nameFieldValue.ToLower().Contains("client") || nameFieldValue.ToLower().Contains("wurst") || nameFieldValue.ToLower().Contains("hack") || nameFieldValue.ToLower().Contains("aristois") || nameFieldValue.ToLower().Contains("addon") || nameFieldValue.ToLower().Contains("fdp") || nameFieldValue.ToLower().Contains("nightware") || nameFieldValue.ToLower().Contains("inertia"))
                                            {
                                                suspiciousWriter.WriteLine("[FORGE] MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.ToString());
                                            }
                                            else { writer.WriteLine("[FORGE] MOD NAME: " + nameFieldValue + "                  FOUND IN (PATH):                 " + path.ToString()); }
                                        }
                                    }
                                    if (monkey) continue;
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        Console.WriteLine(e.Message);
                    }
                }
            }
        }
    }
}