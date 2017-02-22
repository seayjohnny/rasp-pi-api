# Import required libraries
import RPi.GPIO as GPIO
import time
import datetime
import threading

class HallSensorThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)
		self.name = "HallSensorThread"
		self.state = "--";
		self._observers = [];
		
		GPIO.setmode(GPIO.BCM)

		print "Setup GPIO pin as input"

		# Set Switch GPIO as input
		GPIO.setup(18, GPIO.IN)
		GPIO.add_event_detect(18, GPIO.BOTH, callback=self.sensorCallback)
	
	def register_observer(self, observer):
		self._observers.append(observer)
		
	def notify_observer(self, *args, **kwargs):
		for observer in self._observers:
			observer.notify(self, *args, **kwargs)
	
	def sensorCallback(self, channel):
	  # Called if sensor output goes LOW
	  if GPIO.input(18):
		self.state = "Low"
	  else:
		self.state = "High"
	  
	  self.notify_observer()
	
	def run(self):
	  try:
		# Loop until users quits with CTRL-C
		while True :
		  time.sleep(0.1)

	  except KeyboardInterrupt:
		# Reset GPIO settings
		GPIO.cleanup()  
  