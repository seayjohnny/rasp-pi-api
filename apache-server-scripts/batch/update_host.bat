@echo off
pscp -l pi -pw !16root_ c:/rpi/html/*.php pi@192.168.0.2:/var/www/html/
pscp -l pi -pw !16root_ c:/rpi/html/*.html pi@192.168.0.2:/var/www/html/