@echo off
pscp -l pi -pw !16root_ pi@192.168.0.2:/var/www/html/%1 c:/rpi