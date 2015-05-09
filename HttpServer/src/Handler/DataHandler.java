package Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.HttpExchange;

public class DataHandler extends Handler {
	public DataHandler(SimpleHttpServer server) {
		super(server);
	}

	public void handle(HttpExchange t) throws IOException {

		if (authenticate(t)) {

			String command;
			String data1;
			String data2;
			String logg;
			String remove;
			
			//Arduino command

			if (server.reader != null
					&& (command = (String) params.get("command")) != null) {

				server.sendToReader(command);
				response = "";
				send(t);
				return;
			}

			
			// Logg and retrive notes
			
			if ((logg = (String) params.get("logg")) != null) {

				if(!logg.equals("noLogg")){
				server.db.loggNote(logg, username);
				}

				ArrayList<String> temp = server.db.getNotes(username);
				StringBuilder sb = new StringBuilder();

				for (String s : temp) {
					sb.append(s + "<:>");
				}
				response = sb.toString();
				send(t);
				return;


			}
			
			// Remove notes 
			
			if ((remove = (String) params.get("remove")) != null) {

				String[] tempList = remove.split("Posted: ");
				server.db.removeNote(username, tempList[1]);
				ArrayList<String> temp = server.db.getNotes(username);
				StringBuilder sb = new StringBuilder();

				for (String s : temp) {
					sb.append(s + "<:>");
				}
				response = sb.toString();
				send(t);
				return;

			}

			// fetch graph data 
			
			if (((data1 = (String) params.get("data1")) != null)
					&& (data2 = (String) params.get("data2")) != null) {

				ArrayList<Integer> temp = server.db.getData(data1);
				temp.addAll(server.db.getData(data2));
				response = temp.toString();
				send(t);
				return;

			}
		

		} else {

			System.out.println("Identification: Login unsuccessful");

		}
	}
}