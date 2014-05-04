package com121;

import java.net.*;
import java.io.*;

public class Server121 
{
	
	ServerSocket listenSocket = null;
	DataInputStream in;
	DataOutputStream out;
	
	public String Receive(String address,int serverPort, int timeout)
	{
		Boolean stat = false;
		String ans ="";
		try {
			listenSocket = new ServerSocket(serverPort,0,InetAddress.getByName(address));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			listenSocket.setSoTimeout(timeout);
			stat = true;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stat = false;
		}
		if (stat)
		{
			Socket clientSocket = null;
			try {
				clientSocket = listenSocket.accept();
				stat = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				ans ="";
				stat = false;
			}
			if (stat)
			{
				try {
					in = new DataInputStream( clientSocket.getInputStream());
					out =new DataOutputStream( clientSocket.getOutputStream());
					
					ans = in.readUTF();
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			try {
				listenSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return ans;
	}
	
}
