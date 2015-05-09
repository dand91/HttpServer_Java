package Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.HttpExchange;

public class messageDataHandler extends Handler {

	protected HashMap<String, OutputStream> list;
	protected OnlineList onlineList;

	public messageDataHandler(SimpleHttpServer server) {
		super(server);
		this.list = new HashMap<String, OutputStream>();
		this.onlineList = new OnlineList();
	}

	public void handle(HttpExchange t) throws IOException {

		if (authenticate(t)) {

			if (params != null && params.get("message").equals("mLogin")) {

				server.db.mLogin((String) params.get("loginUser"));

				onlineList.addAll(server.db.getOnline());

				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < onlineList.size(); i++) {

					sb.append(onlineList.get(i));
				}
				response = sb.toString();
				send(t);
				return;

			}

			if (params != null && params.get("message").equals("mLogout")) {

				onlineList.clear();
				server.db.mLogout((String) params.get("logoutUser"));
				onlineList.addAll(server.db.getOnline());

				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < onlineList.size(); i++) {

					sb.append(onlineList.get(i));
				}
				response = sb.toString();
				send(t);
				return;

			}

		} else {

			System.out.println("Identification: Login unsuccessful");

		}
	}
}

