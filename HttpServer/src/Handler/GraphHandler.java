package Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;


public class GraphHandler extends Handler {
	public GraphHandler(SimpleHttpServer server) {super(server);}
	public void handle(HttpExchange t) throws IOException {
		
		String command;
		String data1;
		String data2;
		
		if(authenticate(t)){
			
			
			if (server.reader != null
					&& (command = (String) params.get("command")) != null) {

				server.sendToReader(command);
				response = "";
				send(t);
				return;
			}
			
			if (((data1 = (String) params.get("data1")) != null)
					&& (data2 = (String) params.get("data2")) != null) {

				ArrayList<Integer> temp = server.db.getData(data1);
				temp.addAll(server.db.getData(data2));
				response = temp.toString();
				send(t);
				return;

			}

			
		response = server.fileMap.get("graph.html");
		send(t);
	
		}else{
			
			System.out.println("Identification: Login unsuccessful");

		}
	}
}