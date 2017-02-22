from bluetooth import *
import time

server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "6e2121f5-103c-464e-aa0d-2bcc2f8a6cf9"

advertise_service( server_sock, "SampleServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
#                   protocols = [ OBEX_UUID ] 
                    )

print("Waiting for connection on RFCOMM channel %d" % port)

client_sock, client_info = server_sock.accept()
print "Accepted connection from", client_info


try:
	time.sleep(10)

	data = "Connected"
	client_sock.send(data)
	while True:
		data = client_sock.recv(1024)
		if len(data) == 0: break
		print("received [%s]" % data)
		client_sock.send(data)
		print("sent %s" % data)
except IOError:
    pass

print("disconnected")

client_sock.close()
server_sock.close()
print("all done")