@echo off
pscp -l pi -pw !16root_ pi@192.168.0.2:/var/www/html/*.php C:/github/rasp-pi-api/apache-server-scripts/
pscp -l pi -pw !16root_ pi@192.168.0.2:/var/www/html/*.html C:/github/rasp-pi-api/apache-server-scripts/