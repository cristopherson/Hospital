package middleware;

import com121.server121;

import yolopacking.YoloPack;

public class ThreadTcpListener implements Runnable {
	private server121 server;
	private final int TIMEOUT = 0;
	private String data;
	private YoloPack yoloPack;
	private String address;
	private int port;
	private boolean rx_table = false;
	private boolean answer_query = false;

	public ThreadTcpListener(String address, int port) {
		yoloPack = new YoloPack();
		this.server = new server121();
		this.address = address;
		this.port = port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			setData(server.Receive(address, port, TIMEOUT));
			yoloPack.parsePackRx(getData());

			System.out.println("Response = " + yoloPack.getQuery());
			if (yoloPack.getQuery() == QueryType.SET_TABLE) {
				rx_table = true;
			}
			if(yoloPack.getQuery() == QueryType.QUERY_ANSWER){
				
				setAnswer_query(true);
			}
		}

	}

	boolean isRx_table() {
		return rx_table;
	}

	public void setRx_table(boolean rx_table) {
		this.rx_table = rx_table;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isAnswer_query() {
		return answer_query;
	}

	public void setAnswer_query(boolean answer_query) {
		this.answer_query = answer_query;
	}

}
