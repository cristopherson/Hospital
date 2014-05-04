package middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import login.AccessClient;

public class Main extends JFrame{
	
	public Main() {
	}
	
	public void createAndShowGUI(){
		this.setTitle("Yolo Application");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);		
	}

	public static void main(String args[]) throws InterruptedException {
	
//		final Main mainApplication = new Main();
//		javax.swing.SwingUtilities.invokeLater(new Runnable(){
//			public void run(){
//				mainApplication.createAndShowGUI();
//			}
//		});
		
		if (args.length < 2) {
			System.err.println("Usage: Main <Name> <Password>");
			System.exit(-1);
		}

		String id = args[0];
		String pass = args[1];
		String port = "";
		int serverPort = 40000;

		AccessClient RequestLogin = new AccessClient();
		String hostname = "";
		try {
			if (args.length > 2) {
				hostname = args[2];
                                String temp = InetAddress.getLocalHost().getHostAddress()
						.toString();
                                
                                System.out.println( hostname + " = " + temp + "?" + (hostname.toString().equals(temp.toString())));
			} else {
				hostname = InetAddress.getLocalHost().getHostAddress()
						.toString();
			}
			System.out.println("Hostname " + hostname);
			port = RequestLogin.RequestAccess(id, pass, hostname, serverPort);
			if (port.length() == 6) {
				port = port.substring(port.length() - 4, port.length());
			}
			// int newServerPort = Integer.parseInt(port);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		if (port.length() != 4) {
			System.out.println("Login Fail..." + port);
			System.exit(-1);
		} else {
			System.out.println("Current process " + id + ";" + hostname + ";"
					+ port);
		}

		Middleware middleware = new Middleware(id, hostname, port);
		middleware.LoginArbitrationStateMachine();

		Thread listenerThread = new Thread(new MiddlewareListenerThread(
				middleware));
		listenerThread.start();

		System.out.println("Application is ready to send queries");
		while (true) {
			BufferedReader bufferRead = new BufferedReader(
					new InputStreamReader(System.in));
			try {
				String query = bufferRead.readLine();
				middleware.query(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
