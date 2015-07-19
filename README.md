# CodeU
This repo is used for the final project of CodeU. The main purpose of this app is to transferring (mainly apk) files through the networks and possibly auto install them.

# Probelm
The problem we have is that for all kinds of Android devices (Tablet, Phone and Watch), it seems hard to **_install an offline or third-party apk from Computer/Laptop to the device_**. We are trying to solve this problem by letting device be a server and wirelessly (through the Local Area Network) transfer the files from the host machine to the device. Our targetting file type is **apk** file, but might extend to other type of files as well. 

# Solution
There are two big parts for this probelm.
First part is the file transferring part between Android Device and machine. **Security issue can be ignored** right now, but an easy solution will be generating random username and let user pass the username (or password) through the curl command.
<img src="https://github.com/FrankSunChenfan/CodeU/blob/master/pics/High_Level_Thoughts.png" alt="File Transferring Ideas" width="634" height="324" border="10" />
