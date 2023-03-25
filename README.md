# Hack-Detector

A simple Minecraft cheat detector that is written in both C# and Java. It is designed to identify .jar files that contain a fabric.mod.json/mcmod.info file in every subfolder of \AppData\Roaming, Desktop, Downloads, and saves them into a hacks\fabric-forge mods folder on your desktop. 

It will extract the name field within the fabric.mod.json/mcmod.info file of each mod ***(bypassing retarded kids renaming their hacks into optifine 🥰)*** and store them into "! Suspicious.txt". The detector also saves the most suspicious files (99.9% chance of being hacks) into "!!! Extremely Suspicious.txt". 

Furthermore, it also checks for .minecraft versions to detect non fabric/forge cheats 🥰💥🐦💥🐦🥶

The process can take up to 10 seconds

Example:
![jjj](https://user-images.githubusercontent.com/109868859/227728831-fafc25b0-7b06-468c-9bf2-8f335a83c655.png)


***Usage:***

run the beau.exe or the .jar file and it will create a folder like this on your desktop
![image](https://user-images.githubusercontent.com/109868859/227728878-37b5cbd0-63aa-442e-9c06-e597200c207a.png)
