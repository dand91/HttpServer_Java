package Reader;

import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Stream;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import Database.Database;
import HttpServer.SimpleHttpServer;


/**
 * @author nossredan
 * 
 * Class for making sure that future readers will be observable and runnable, as well as making 
 * sure that the COM port is closed properly when needed, outside of the class.
 */
public abstract class SerialReader implements SerialPortEventListener, Runnable, Observer{
	
	protected StringBuilder sb = new StringBuilder();
	protected SerialPort serialPort;	
	
	public SerialReader(String port, SimpleHttpServer server) throws SerialPortException {
		
		server.addObserver(this);
					
		 serialPort	= new SerialPort(port);
		 serialPort.openPort();
		 serialPort.setParams(9600, 8, 1, 0);
		 serialPort.addEventListener(this);
	}
	
	/**
	 * Method for adding to the stream of information currently at COM port. 
	 * stream is represented by a String builder sb. 
	 */
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		
		
		try {	
			sb.append(serialPort.readString());

		} catch (SerialPortException e) {
				
			e.printStackTrace();
			closePort();
		}
	}
	@Override
	public void update(Observable o, Object arg){
			
		try {
			
			System.out.print("Printed: " + (String)arg  + " ");
			System.out.println(serialPort.writeString((String)arg + "*"));

		} catch (SerialPortException e) {
				
			e.printStackTrace();
			closePort();
		}
		
		}
	public void closePort() {

		try {
			
			serialPort.closePort();
			
		} catch (SerialPortException e) {
			
			e.printStackTrace();
		}

	}	
}
