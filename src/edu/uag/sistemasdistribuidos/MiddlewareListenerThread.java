package edu.uag.sistemasdistribuidos;

public class MiddlewareListenerThread implements Runnable{
	private Middleware middleware;

	public MiddlewareListenerThread(Middleware middleware){
		this.middleware = middleware;		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
			middleware.txMemberList();
	}

}
