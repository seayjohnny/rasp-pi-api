@echo off
pscp -l pi -pw !16root_ C:/github/rasp-pi-api/apache-server-scripts/*.php pi@192.168.0.2:/var/www/html/
pscp -l pi -pw !16root_  C:/github/rasp-pi-api/apache-server-scripts/*.html pi@192.168.0.2:/var/www/html/