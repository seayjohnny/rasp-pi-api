@echo off
pscp -l pi -pw !16root_ %1 pi@192.168.0.2:/var/www/html
