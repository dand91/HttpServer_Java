package Handler;

import java.io.IOException;
import java.io.OutputStream;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


 public class LoginHandler extends Handler {
	public LoginHandler(SimpleHttpServer server) {super(server);}
	public void handle(HttpExchange t) throws IOException {

		response = server.fileMap.get("startPage.html");
		send(t);
	}
}