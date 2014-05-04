package login;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class AccessClient {
	public String RequestAccess(String Name, String Password, String hostname, int serverPort) 
	{
		// arguments supply message and hostname of destination
		Socket s = null;
		String ans = "";
		try
		{
			
			s = new Socket(hostname, serverPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream out = new DataOutputStream( s.getOutputStream());
			out.writeUTF(Name+","+Password);
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
			if(s!=null) 
			{
				try 
				{
					s.close();				
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
