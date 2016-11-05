
import RPi.GPIO as GPIO
from time import sleep
import datetime
from firebase import firebase
import pigpio
import DHT22
import urllib2, urllib, httplib
import json
import os 
from functools import partial

# Global variables
LOGGER = 1
buttonTest = 23
sensor_pin_number = 21;
firebase = firebase.FirebaseApplication('https://dashmotic.firebaseio.com', None)


# LOGGER
def printlog(text):
    if(LOGGER):
        print(text)
# LOGGER


# Start script
# ============================================================================
printlog("Script Started\n")

#Information about temperature, humidity, cpu, disk, RAM
# Initiate GPIO for pigpio
pi = pigpio.pi()
# Setup the sensor
dht22 = DHT22.sensor(pi, sensor_pin_number)
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


def updatePiInfo():

    printlog("## Updating Firebase Info.. ##")
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
        printlog("Updated Humidity max_inside")
    if float(temperature) > float(maxTemperature):
        firebase.put("/Controls/Sensors", "/Temperature/max_inside", ""+temperature+"C")
        printlog("Updated Temperature max_inside")
        
    ## cehck for min values
    if float(humidity) < float(minHumidity):
        firebase.put("/Controls/Sensors", "/Humidity/min_inside", ""+humidity+"%")
        printlog("Updated Humidity min_inside")
    if float(temperature) < float(minTemperature):
        firebase.put("/Controls/Sensors", "/Temperature/min_inside", ""+temperature+"C")
        printlog("Updated Temperature min_inside")

    #CPU INFO
    CPU_temp = getCPUtemperature()
    CPU_usage = getCPUuse()
    firebase.put("/PI/CPU", "/temperature", CPU_temp)

    #RAM INFO
    RAM_stats = getRAMinfo()
    RAM_total = round(int(RAM_stats[0]) / 1000,1)
    RAM_used = round(int(RAM_stats[1]) / 1000,1)
    RAM_free = round(int(RAM_stats[2]) / 1000,1)
    firebase.put("/PI/RAM", "/free", str(RAM_free)+"")
    firebase.put("/PI/RAM", "/used", str(RAM_used)+"")
    firebase.put("/PI/RAM", "/total", str(RAM_total)+"")

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


    printlog(datetime.datetime.now().strftime(dateString))
    printlog("Humidity: Current["+humidity+"], Max["+maxHumidity+"], Min["+minHumidity+"]")
    printlog("Temperature: Current["+temperature+"], Max["+maxTemperature+"], Min["+minTemperature+"]")
    printlog("CPU temperature: "+CPU_temp)
    printlog("RAM total["+str(RAM_total)+" MB], RAM used["+str(RAM_used)+" MB], RAM free["+str(RAM_free)+" MB]")
    printlog("DISK total["+str(DISK_total)+"], free["+str(DISK_free)+"], perc["+str(DISK_perc)+"]")
    printlog("## Update finished successfully ##")
    printlog("======================================================\n")




# ============================================================================================
# GPIO CALLBACKS FOR PHYSICAL BUTTONS
GPIO.setmode(GPIO.BOARD)

switch_number_key = 'switch_number'
pin_number_key = 'pin_number'

def toggleControlChild(name, pin_number, control_type, channel):
    # First switch on/off the light in relation of the gpio state
    GPIO.setup(pin_number, GPIO.OUT)
    value = GPIO.input(pin_number)
    newValue = int(not value)
    GPIO.output(pin_number, newValue)

    # Then update firebase with the new state
    firebase.put("/Controls/"+control_type+"/"+name, "/value", newValue)



# Fetch firebase switch data
controls = firebase.get('/Controls', None)

for cat_name, cat_value in controls.items():
    for control_name, control_value in cat_value.items():
        if control_value.get(switch_number_key) != None or control_value.get(switch_number_key) is not None:
            # Set up IN gpio pin for switch
            switch_number = control_value.get(switch_number_key)
            GPIO.setup(switch_number, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
            # register callback for each switch retrieved
            GPIO.add_event_detect(switch_number, GPIO.BOTH, callback=partial(toggleControlChild, control_name, control_value.get(pin_number_key), cat_name), bouncetime=1000)


while True:
    try:
        updatePiInfo()
        print("")

        #Retrieve sleep time from firebase and continue the loop
        sleepTime = firebase.get("/Settings/info_update_time_interval", None)
        sleepTime = int(sleepTime)
        sleep(sleepTime)
    except:
        continue








