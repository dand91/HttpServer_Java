package Reader;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.StringTokenizer;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import Database.Database;
import HttpServer.SimpleHttpServer;

public class ArduinoReader extends SerialReader {

	private StringTokenizer st;
	private Database db;
	private String regex = "[0-9]+";

	public ArduinoReader(String port, SimpleHttpServer server)
			throws SerialPortException {

		super(port, server);

		try {

			this.db = Database.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {

			long start = System.currentTimeMillis();

			while (true) {

				long end = System.currentTimeMillis();

				if (end - start > 200) {

					start = end;

					soundInfo("A");
					soundInfo("B");

				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			closePort();
		}
	}

	private void soundInfo(String s) {
		
		try{

		String string = sb.toString();
		String newString = "";

		if (string.contains(s) && string.contains(s + "*")) {

			int start = string.indexOf(s);
			int end = string.indexOf(s + "*");

			newString = (String) sb.substring(start + 2, end - 1);
			sb.delete(start, end + 2);

			if (db.isConnected() && newString.matches(regex)) {
				db.loggData(newString, s);
				
			} else {
				
				System.out.println("Reader: Malformed read >" + newString + "<");
			}
		}
		
		}catch(StringIndexOutOfBoundsException e ){
			
			System.out.println("Reader: Malformed read");
			sb.setLength(0);

		}
	}
}
