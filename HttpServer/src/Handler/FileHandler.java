package Handler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;


public class FileHandler extends Handler {
	public FileHandler(SimpleHttpServer server) {super(server);}
	public void handle(HttpExchange t) throws IOException {
		
		if(authenticate(t)){

		response = server.fileMap.get("file.html");
		response = insertAddress(response);

		BufferedInputStream in = new BufferedInputStream(t.getRequestBody());

		BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody()));

		try {
		File file = new File("test");
		FileOutputStream out = new FileOutputStream(file);

		if (!file.exists()) {
			file.createNewFile();
		}

		final byte data[] = new byte[1024];
		int count;
		String line;
		
			while ((count = in.read(data, 0, 1024)) != -1) {
				out.write(data, 0, count);
			}
			
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		send(t);

	
		}else{
			
			System.out.println("Identification: Login unsuccessful");

		}
	}
}