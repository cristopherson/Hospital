package com121;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class client121 {
	public Boolean Send(String message, String hostname, int serverPort) 
	{
		// arguments supply message and hostname of destination
		Socket s = null;
		Boolean ans = false;
		try
		{
			
			s = new Socket(hostname, serverPort);
			//DataInputStream in = new DataInputStream( s.getInputStream());
			DataOutputStream out = new DataOutputStream( s.getOutputStream());
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
			//System.out.println("IO:"+e.getMessage());
			ans = false;
		}
		finally 
		{
			if(s!=null) 
			{
				try 
				{
					s.close();
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
