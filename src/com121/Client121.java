package com121;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client121 {
	public Boolean send(String message, String hostname, int serverPort) 
	{
		// arguments supply message and hostname of destination
		Socket socket = null;
		Boolean ans = false;
		try
		{
			
			socket = new Socket(hostname, serverPort);
			//DataInputStream in = new DataInputStream( s.getInputStream());
			DataOutputStream out = new DataOutputStream( socket.getOutputStream());
			out.writeUTF(message);
			ans = true;
		}
		catch (UnknownHostException e)
		{
			System.out.println("Sock:"+e.getMessage());
		}
		catch (EOFException e)
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch (IOException e)
		{
		ans = false;
		}
		finally 
		{
			if(socket!=null) 
			{
				try 
				{
					socket.close();
					System.out.println("closed");
				}
				catch (IOException e)
				{
					System.out.println("close:"+e.getMessage());
				}
			}
		}
		return ans;
	}

}
