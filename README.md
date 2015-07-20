# CodeU
This repo is used for the final project of CodeU. The main purpose of this app is to transfer (mainly apk) files through the networks and possibly auto install them.

# Probelm
The problem we have is that for all kinds of Android devices (Tablet, Phone and Watch), it seems hard to **_install an offline or third-party apk from Computer/Laptop_**. We are trying to solve this problem by letting the device be a server and wirelessly (through the Local Area Network) transfer the files from the host machine to the device. Our targetting file type is **apk** file, but might extend to other type of files as well. 

# Solution
There are three big parts for this probelm.

### File Transferring
First part is the file transferring part between Android Device and machine. **Security issue can be ignored** right now, but an easy solution will be generating random username and let user pass the username (or password) through the curl command.

<img src="https://github.com/FrankSunChenfan/CodeU/blob/master/pics/High_Level_Thoughts.png" alt="File Transferring Ideas" width="634" height="324" border="10" />

The above image shows the general and easiest idea to transfer a file from host machine to device. There is one thing might need to be keep in mind -  
the energy consumption issue - normal server will take a lot of energy in order to become functional. We certainly won't do something fancy like a node js server. That part needs some research and trying to figure out a lightweright solution but reliable architecture. 

### File Management 
The storage issue - or saying the file manage system inside the device. When and where should we store them, how do we show them to the user, and whether we should set timer to delete the file. 

**A suggested solution** will be checking the timestamp difference between the file and the current system time, if the diff is > 24 hrs we delete the file. Additionally, we can also let user delete file themselves, or mv the file to other directory - so that we don't bother manage them.
An index system will be a plus. A brief plan can be showed below.

<img src="https://github.com/FrankSunChenfan/CodeU/blob/master/pics/Timestamp_Idea.png" alt="Timestamp Idea">

You can see that that after more than 24 hrs of staying in the cache file system, the file will be defaultly deleted. This process can be done when firing up the app, or during background as a service. We can even give user alert when we are going to delete the apk. I think this solution will work.

About user and file system interaction I think can be summarized as the following:

<img src="https://github.com/FrankSunChenfan/CodeU/blob/master/pics/Manual_Option.png" alt="Manual Option">

This will be all the options that user can potentially do manually to a file I will say. Need some research on how to achieve these functionalities as well. 

Another thing mainly associates with file system is the GUI of our app. I would think it as an easy or not complicated clean app. If our app is popular maybe we can insert some ads to make some pocket money :) jk. 

My thoughts for the GUI designed will be something like:

<img src="https://github.com/FrankSunChenfan/CodeU/blob/master/pics/GUI_Simple.png" alt="GUI Simple">

### Installation Related
This one has no detailed explanation, but will probably required some research on the permission system of Android. I personally don't think auto installation will be allowed on Android. The auto installation is defined as something like "sudo apt-get install -y docker". The auto installation will probably need root permission in order to be functional - which we can definitely dig into.

A step-back solution will be our program shows the permission info page like the below image, and then user just need to press "confirm" so that the installation process will be started. 

<img src="http://www.androidpolice.com/wp-content/uploads/2013/07/nexusae0_wm_2013-07-25-12.51.491.png" alt="Permission info" height="853" width="512">

