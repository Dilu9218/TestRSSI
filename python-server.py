import socket

#UDP_IP = "192.168.1.33"
UDP_IP = "192.168.43.97"
UDP_PORT = 10500

sock = socket.socket(socket.AF_INET, # Internet
	socket.SOCK_DGRAM) # UDP

sock.bind((UDP_IP, UDP_PORT))

print "Waiting..."

while True:
  data, addr = sock.recvfrom(1024) # buffer size is 1024 bytes
  print "-----------------------------------------------------\n", data
