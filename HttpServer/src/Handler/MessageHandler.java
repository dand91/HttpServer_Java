package Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import HttpServer.SimpleHttpServer;

import com.sun.net.httpserver.HttpExchange;

public class MessageHandler extends Handler{
	
	HashMap<String, sendThread> threadList;
	HashMap<String, Ob> observerList;
	OnlineList onlineList;
	Ob o;
	
	public MessageHandler(SimpleHttpServer server) {
		super(server);
		this.threadList = new HashMap<String, sendThread>();
		this.observerList = new HashMap<String, Ob>();
		this.onlineList = new OnlineList();
		
	}

	public void handle(HttpExchange t) throws IOException {
		
			
		if (authenticate(t)) {			
			
			
			if(params.get("sendMessage") != null){
				
				
				if ( ((String)params.get("sendMessage")).equals("<?>")  ) {
					
					if(!threadList.containsKey(username)){
						
					Ob o = new Ob();
					observerList.put(username, o);
					threadList.put(username, new sendThread(username, t, o));
					
					}else{
						
						response = "";
						send(t);
					}
					return;
				}
				
				String toUser = ((String)params.get("toUser"));
				String message = (String)params.get("sendMessage");
				
				for(Entry<String, sendThread> s : threadList.entrySet()){
					
					if(s.getKey().equals(toUser)){
						
						observerList.get(toUser).notifyer("From " + username + ": " +  message);
						threadList.remove(toUser);
						observerList.remove(toUser);
						
					}
				}
				
				response = "";
				send(t);
				return;
			}
			
			if(params.get("message") != null){
				

			if (params.get("message").equals("mLogin")) {

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

			if (params.get("message").equals("mLogout")) {

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
			}

			response = server.fileMap.get("message.html");
			send(t);
						
		} else {

			System.out.println("Identification: Login unsuccessful");

		}
	}
}

class OnlineList {

	public ArrayList<String> list;

	public OnlineList() {

		this.list = new ArrayList<String>();
	}

	public void clear() {

		list.clear();
	}

	public String get(int i) {
		return list.get(i);
	}

	public int size() {

		return list.size();
	}

	public void addAll(List<String> newList) {

		for (String s : newList) {

			if (!list.contains(s)) {
				list.add(s);
			}
		}
	}
}

class sendThread extends Thread implements Observer{
	
	String name;
	String message = "noSend";
	HttpExchange t;
	
	 private final Object monitor = new Object();
	 private boolean pauseThreadFlag = false;
	
	public sendThread (String name, HttpExchange t, Ob o){
		
		o.addObserver(this);
		this.name = name;
		this.t = t;
		start();
	}
	
	public void run(){

		while(true){
			
	            checkForPaused();
	            
		if(!message.equals("noSend")){
			
			String s = message;
			try {

				t.sendResponseHeaders(200, s.length());
				OutputStream os = t.getResponseBody();
				os.write(s.getBytes());
				os.close();

			} catch (IOException e) {
			}
			return;

		}
	}
	}

    private void checkForPaused() {
        synchronized (monitor) {
            while (pauseThreadFlag) {
                try {
                    monitor.wait();
                } catch (Exception e) {}
            }
        }
    }

    public void pauseThread() throws InterruptedException {
        pauseThreadFlag = true;
    }

    public void resumeThread() {
        synchronized(monitor) {
            pauseThreadFlag = false;
            monitor.notify();
        }
    }
	@Override
	public void update(Observable o, Object arg) {
		this.message = (String)arg;
		resumeThread();		

	}

}

class Ob extends Observable{
	
	public void notifyer(String message){
		
		this.setChanged();
		this.notifyObservers(message);
	}
	
}