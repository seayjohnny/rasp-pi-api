@echo off
pscp -l pi -pw !16root_ pi@192.168.0.2:/var/www/html/*.php c:/rpi
pscp -l pi -pw !16root_ pi@192.168.0.2:/var/www/html/*.html c:/rpi