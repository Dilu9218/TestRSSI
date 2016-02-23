import os
import tempfile
import time
import socket

filename = '/home/asanka/Downloads/myfifo'
print filename
try:
	os.mkfifo(filename)
except OSError, e:
	print "Failed to create FIFO: %s" % e

bufsize=1 # line buffered where each line is flushed immediately to the file
fifo = open(filename, 'w', bufsize)


#UDP_IP = "192.168.1.33"
UDP_IP = "192.168.43.97"
UDP_PORT = 10500

sock = socket.socket(socket.AF_INET, # Internet
	socket.SOCK_DGRAM) # UDP

sock.bind((UDP_IP, UDP_PORT))

print "Waiting..."

while True:
	data, addr = sock.recvfrom(1024) # buffer size is 1024 bytes
	print "-----------------------------------------------------\n", data.lower()
	fifo.write(data.lower())
	fifo.write('\n')	
