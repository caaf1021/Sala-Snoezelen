from socket import *
from thread import *
import serial

ser=serial.Serial('/dev/ttyUSB0',9600, timeout=1)
print 'Conectado Serial'

ip_servidor = '192.168.20.106'
puerto = 12345


soc = socket()

soc.bind((ip_servidor,puerto))
soc.listen(5)

r = ""

def imprimir(conexion):


    
    try:
        while True:
	    	r = conexion.recv(1024)
            	r = r.replace('\n','')
		
            	if r == 'cerrar':
                	break
            	elif not r :
                	break
            	else:
               		ser.write(r)
               		
    except KeyboardInterrupt:
        conexion.close()
        soc.close()
        exit

def recibir():

    try:
        while True:

		datos = ser.readline()
		
		modulos = datos.split(',')
	
		if modulos[0] == 'M06':
			u = modulos[0]+','+modulos[1]+','+modulos[2]
			ser.write(u)
	
    except KeyboardInterrupt:
        conexion.close()
        soc.close()
        exit



try:
    
    start_new_thread(recibir,())

    while True:

        c,addr = soc.accept()
        start_new_thread(imprimir,(c,))

except Exception:
    c.close()
    soc.close()
    exit


soc.close()
c.close()
    
