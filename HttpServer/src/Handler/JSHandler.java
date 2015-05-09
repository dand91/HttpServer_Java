package Handler;

import java.io.IOException;
import java.io.OutputStream;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class JSHandler extends Handler{
	public JSHandler(SimpleHttpServer server) {super(server);}
	public void handle(HttpExchange t) throws IOException {
		
		String[] temp = t.getRequestURI().toString().split("/");
		response = server.fileMap.get(temp[temp.length - 1]);
		send(t);

	}
}
