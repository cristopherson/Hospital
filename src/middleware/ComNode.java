package middleware;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ComNode implements Node {

    private MulticastSocket listen;
    private MulticastSocket sender;
    private String address;
    public static int defaulPort = 55558;

    @Override
    public boolean access(String address) {
        // TODO Auto-generated method stub
        this.address = address;
        try {
            listen = new MulticastSocket(ComNode.defaulPort);
            sender = new MulticastSocket();

            listen.joinGroup(InetAddress.getByName(this.address));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void send(String message) {
        // TODO Auto-generated method stub
        DatagramPacket dgp;
        try {
            dgp = new DatagramPacket(message.getBytes(),
                    message.getBytes().length,
                    InetAddress.getByName(this.address), ComNode.defaulPort);
            sender.send(dgp);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public byte[] receive(int timeout) {
        // TODO Auto-generated method stub

        byte data[] = new byte[1024];
        DatagramPacket dgp = new DatagramPacket(data, data.length);

        if (timeout >= 0) {
            try {
                listen.setSoTimeout(timeout);
            } catch (SocketException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
        }

        try {
            listen.receive(dgp);
        } catch (SocketTimeoutException ex) {
            // ignore it for now
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        data = dgp.getData();
        return data;
    }

}
