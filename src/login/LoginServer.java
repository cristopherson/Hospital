package login;

import java.net.*;
import java.io.*;

public class LoginServer 
implements Runnable 
{
	public void run() 
	{
		try
		{
			String address = InetAddress.getLocalHost().getHostAddress().toString();

			int serverPort = 40000;
			ServerSocket listenSocket = new ServerSocket(serverPort,0,InetAddress.getByName(address));			
			
			while(true) 
			{
				try
				{
					Socket clientSocket = listenSocket.accept();
					Connection connection = new Connection(clientSocket);
					Thread.sleep(100);
				}
				catch (InterruptedException e) 
				{	            
					break;
				}
			}
		} 
		catch(IOException e) 
		{
			System.out.println("Listen :"+e.getMessage());
		}
	}
}

class Connection extends Thread 
{
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;

	
	
	public Connection (Socket aClientSocket) 
	{
		try 
		{					
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
			out.flush();
			this.start();
		} 
		catch(IOException e) 
		{
			System.out.println("Connection:"+e.getMessage());
		}
	}
			
	public void run()
	{
		String name = "";
		String pass = "";
		String ans = "";
		try 
		{
			//Get user and password from client
			String[] array = in.readUTF().split(",");
			if (array.length ==2)
			{
				name = array[0];
				pass = array[1];
			}
			System.out.println("");
			System.out.println("Access requested by:" + name);
			
			//Search for it
			String sCurrentLine;
			String file = "AccessList.txt";	
			FileReader fr = new FileReader(file);
			BufferedReader textReader = new BufferedReader(fr);
			int i = 0;
			while ((sCurrentLine = textReader.readLine()) != null) 
			{
				array = sCurrentLine.split(",");
				if (array.length ==3)
				{
					if ((name.equals(array[0])) && (pass.equals(array[1])))
					{
						System.out.println("Access granted");
						ans =array[2];
						break;
						
					}
				}
				i++;
			}
			textReader.close();
			out.flush();	
			out.writeUTF(ans);
		} 
		catch(EOFException e) 
		{
			System.out.println("EOF:"+e.getMessage());
		} 
		catch(IOException e) 
		{
			System.out.println("IO:"+e.getMessage());
		}		 
		finally
		{
			try 
			{
				clientSocket.close();
			}
			catch (IOException e)
			{
				/*close failed*/
			}
		}
		
	}
}