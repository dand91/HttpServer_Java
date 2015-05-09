package Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;


public class NoteHandler extends Handler {
	public NoteHandler(SimpleHttpServer server) {super(server);}
	public void handle(HttpExchange t) throws IOException {
		
		String logg;
		String remove;
		
		if(authenticate(t)){
			
			
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


		response = server.fileMap.get("note.html");
					
		send(t);

	
		}else{
			
			System.out.println("Identification: Login unsuccessful");

		}
	}
}