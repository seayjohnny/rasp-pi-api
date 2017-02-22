from bluetooth import *
import time
import threading
from hall_sensor import *

try:
	hs = HallSensorThread()
	hs.start()
except:
	print "Thread 1 Error"

class SensorObserverThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)
		self.hs_flag = 0
		self.state = hs.state
	
	def run(self):
		while True:
			if self.state != hs.state:
				self.hs_flag = 1
				self.state = hs.state
				print("hs_flag 1")
				
	
class BTServerThread(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)
		self.server_sock=BluetoothSocket( RFCOMM )
		self.server_sock.bind(("",1))
		self.server_sock.listen(1)

		self.port = self.server_sock.getsockname()[1]
		self.uuid = "6e2121f5-103c-464e-aa0d-2bcc2f8a6cf9"
		
	def notify(self, observable):
		self.hs_flag = 1;
		print("hs_flag 1")
		
	def send(self, message):
		self.client_sock.send(message)
		print("Sent %s" % message)
		
	def run(self):
		advertise_service( self.server_sock, "SampleServer",
						   service_id = self.uuid,
						   service_classes = [ self.uuid, SERIAL_PORT_CLASS ],
						   profiles = [ SERIAL_PORT_PROFILE ], 
		#                   protocols = [ OBEX_UUID ] 
							)

		print("Waiting for connection on RFCOMM channel %d" % self.port)

		self.client_sock, self.client_info = self.server_sock.accept()
		print "Accepted connection from", self.client_info

		try:
			time.sleep(10)

			data = "Connected"
			self.send(data)
			data = "[301]"
			self.send(data)
			while True:
				data = self.client_sock.recv(1024)
				if len(data) == 0: break 
				if data == "[302]":
					print("received sensor state request")
					data = "[303] %s" % hs.state
					self.send(data)
				else:
					print("received test message")
					self.send(data)
		except IOError:
			pass


		print("disconnected")

		self.client_sock.close()
		self.server_sock.close()
		print("all done")

try:
	btServer = BTServerThread()
	btServer.start()
except:
	print "Thread 2 Error"