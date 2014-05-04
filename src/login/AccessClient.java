package login;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class AccessClient {
	public String requestAccess(String name, String password, String hostname, int serverPort) 
	{
		// arguments supply message and hostname of destination
		Socket socket = null;
		String ans = "";
		try
		{
			
			socket = new Socket(hostname, serverPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream out = new DataOutputStream( socket.getOutputStream());
			out.writeUTF(name+","+password);
			if ((ans = in.readLine()) == null) 
			{
				ans ="ERROR";
			}

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
			ans = "";
		}
		finally 
		{
			if(socket!=null) 
			{
				try 
				{
					socket.close();				
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
