@echo off
pscp -l pi -pw !17root_ c:/rpi/python/*.py pi@192.168.0.4:/home/pi/py_scripts/
