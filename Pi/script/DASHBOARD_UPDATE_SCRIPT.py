
import RPi.GPIO as GPIO
from time import sleep
import datetime

import urllib2, urllib, httplib
import pigpio
import DHT22
import os 
GPIO.setmode(GPIO.BOARD)


#============================================================================
#Information about temperature, humidity, cpu, disk, RAM

# Initiate GPIO for pigpio
pi = pigpio.pi()
# Setup the sensor
dht22 = DHT22.sensor(pi, 21) # gpio pin name for sensor
dht22.trigger()


def readDHT22():
    # Get a new reading
    dht22.trigger()
    # Save our values
    humidity  = '%.2f' % (dht22.humidity())
    temp = '%.2f' % (dht22.temperature())

    return (humidity, temp)


# Return CPU temperature as a character string                                      
def getCPUtemperature():
    res = os.popen('vcgencmd measure_temp').readline()
    return(res.replace("temp=","").replace("'C\n",""))

# Return RAM information (unit=kb) in a list                                        
# Index 0: total RAM                                                                
# Index 1: used RAM                                                                 
# Index 2: free RAM                                                                 
def getRAMinfo():
    p = os.popen('free')
    i = 0
    while 1:
        i = i + 1
        line = p.readline()
        if i==2:
            return(line.split()[1:4])

# Return % of CPU used by user as a character string                                
def getCPUuse():
    return(str(os.popen("top -n1 | awk '/Cpu\(s\):/ {print $2}'").readline().strip(\
)))

# Return information about disk space as a list (unit included)                     
# Index 0: total disk space                                                         
# Index 1: used disk space                                                          
# Index 2: remaining disk space                                                     
# Index 3: percentage of disk used                                                  
def getDiskSpace():
    p = os.popen("df -h /")
    i = 0
    while 1:
        i = i +1
        line = p.readline()
        if i==2:
            return(line.split()[1:5])

##Date time formatting
dateString = '%d/%m/%Y %H:%M:%S'


##Main function used to update al the informations
def updatePIInfo():
        
    from firebase import firebase
    firebase = firebase.FirebaseApplication('https://dashmotic.firebaseio.com', None)

    print(datetime.datetime.now().strftime(dateString))
    firebase.put("/Settings", "/last_update_datetime", datetime.datetime.now().strftime(dateString))

    ##retrive max & min humidity (remove the %)
    maxHumidity = firebase.get("/Controls/Sensors/Humidity/max_inside", None)
    maxHumidity = maxHumidity[:-1]

    minHumidity = firebase.get("/Controls/Sensors/Humidity/min_inside", None)
    minHumidity = minHumidity[:-1]

    #retrieve max & min temperature (remove the C)
    maxTemperature = firebase.get("/Controls/Sensors/Temperature/max_inside", None)
    maxTemperature = maxTemperature[:-1]

    minTemperature = firebase.get("/Controls/Sensors/Temperature/min_inside", None)
    minTemperature = minTemperature[:-1]

    #add current value
    humidity, temperature = readDHT22()
    firebase.put("/Controls/Sensors", "/Humidity/current_inside", ""+humidity+"%")
    firebase.put("/Controls/Sensors", "/Temperature/current_inside", ""+temperature+"C")

    ##check for max values
    if float(humidity) > float(maxHumidity):
        firebase.put("/Controls/Sensors", "/Humidity/max_inside", ""+humidity+"%")
        print("Updating Humidity max_inside")
    if float(temperature) > float(maxTemperature):
        firebase.put("/Controls/Sensors", "/Temperature/max_inside", ""+temperature+"C")
        print("Updating Temperature max_inside")
        
    ## cehck for min values
    if float(humidity) < float(minHumidity):
        firebase.put("/Controls/Sensors", "/Humidity/min_inside", ""+humidity+"%")
        print("Updating Humidity min_inside")
    if float(temperature) < float(minTemperature):
        firebase.put("/Controls/Sensors", "/Temperature/min_inside", ""+temperature+"C")
        print("Updating Temperature min_inside")

    print("Humidity: Current["+humidity+"], Max["+maxHumidity+"], Min["+minHumidity+"]")
    print("Temperature: Current["+temperature+"], Max["+maxTemperature+"], Min["+minTemperature+"]")

    #CPU INFO
    CPU_temp = getCPUtemperature()
    CPU_usage = getCPUuse()
    firebase.put("/PI/CPU", "/temperature", CPU_temp)
    print("CPU temperature: "+CPU_temp)

    #RAM INFO
    RAM_stats = getRAMinfo()
    RAM_total = round(int(RAM_stats[0]) / 1000,1)
    RAM_used = round(int(RAM_stats[1]) / 1000,1)
    RAM_free = round(int(RAM_stats[2]) / 1000,1)
    firebase.put("/PI/RAM", "/free", str(RAM_free)+"")
    firebase.put("/PI/RAM", "/used", str(RAM_used)+"")
    firebase.put("/PI/RAM", "/total", str(RAM_total)+"")
    print("RAM total["+str(RAM_total)+" MB], RAM used["+str(RAM_used)+" MB], RAM free["+str(RAM_free)+" MB]")

    #DISK INFO
    DISK_stats = getDiskSpace()
    DISK_total = DISK_stats[0]
    DISK_free = DISK_stats[1]
    DISK_perc = DISK_stats[3]
    DISK_used = float(DISK_total[:-1]) - float(DISK_free[:-1])
    firebase.put("/PI/DISK", "/total", str(DISK_total[:-1]))
    firebase.put("/PI/DISK", "/free", str(DISK_free[:-1]))
    firebase.put("/PI/DISK", "/used", str(DISK_used))
    firebase.put("/PI/DISK", "/percentage", str(DISK_perc))
    print("DISK total["+str(DISK_total)+"], free["+str(DISK_free)+"], perc["+str(DISK_perc)+"]")
    print("======================================================")



while True:

    try:

        updatePIInfo()

    except:
	print("=!=!=!=!=!=!=!=!= EXCEPTION RAISED NOW =!=!=!=!=!=!=!=!=!!=!=!=!=!") 
        continue

    #Retrieve sleep time from firebase and continue the loop
    from firebase import firebase
    firebase = firebase.FirebaseApplication('https://dashmotic.firebaseio.com', None)
    sleepTime = firebase.get("/Settings/info_update_time_interval", None)
    sleepTime = int(sleepTime)
    sleep(sleepTime)










