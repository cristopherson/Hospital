package middleware;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MiddlewareListenerThread implements Runnable{
	private Middleware middleware;

	public MiddlewareListenerThread(Middleware middleware){
		this.middleware = middleware;		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
                    try {
                        Thread.sleep(5);
                        middleware.txMemberList();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MiddlewareListenerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
	}

}
