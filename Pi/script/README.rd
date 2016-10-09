How to start the dashboar script in background:

1. after boot run command "sudo pigpiod" to start the sudo demon pi
2. run the script in background on the pi with the command
"nohup python DASHBOARD_PRODUCTION_SCRIPT.py &"
3. now we can close the terminal and the script will run in background
