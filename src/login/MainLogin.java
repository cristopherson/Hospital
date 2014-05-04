package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainLogin {

    public static void main(String[] args) {
        Boolean run = true;
        Thread thread = new Thread(new LoginServer());
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (run) {
            String fromUser = "";
            BufferedReader readConsole = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Exit Login Server? (Y/N): ");
            try {
                fromUser = readConsole.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (fromUser.equals("Y")) {
                run = false;
            }
            /*try {
             Thread.sleep(1000);
             } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             }*/
        }

        thread.interrupt();
        System.exit(0);
    }

}
