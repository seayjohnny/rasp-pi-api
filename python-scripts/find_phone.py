import bluetooth as bt

print "Performing discovery...."
nearby_devices = bt.discover_devices(lookup_names = True)

print "Found %d devices" % len(nearby_devices)

for name, addr in nearby_devices:
	print " %s - %s " % (addr, name)
	
