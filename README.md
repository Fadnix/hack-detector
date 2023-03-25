# Hack-Detector

A simple Minecraft cheat detector that is written in both C# and Java. It is designed to identify .jar files that contain a fabric.mod.json file in every subfolder of \AppData\Roaming, Desktop, Downloads, and saves them into a hacks\fabric-mods folder on your desktop. 

It will extract the name field within the fabric.mod.json file of each mod ***(bypassing retarded kids renaming their hacks to optifine 🥰)*** and store them into "! Suspicious.txt". The detector also saves the most suspicious files (99.9% chance of being hacks) into "!!! Extremely Suspicious.txt". 

Furthermore, it also checks for .minecraft versions to detect non-fabric cheats 🥰💥🐦💥🐦🥶

Example:
![image](https://user-images.githubusercontent.com/109868859/227674728-e57a7c40-fa91-41af-9adf-53b139f2dc1b.png)
soon: forge hacks detection
