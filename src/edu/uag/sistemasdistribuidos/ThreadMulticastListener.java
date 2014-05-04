package edu.uag.sistemasdistribuidos;

import java.io.UnsupportedEncodingException;

import yolopacking.YoloPack;

public class ThreadMulticastListener implements Runnable {
	private ComNode node;
	private final int TIMEOUT = 0;
	private String data;
	private YoloPack yoloPack;
	private boolean rx_inquire;
	private boolean login_requested;
	private boolean update_table;
	private boolean query_requested;
	private boolean query_answer;

	public ThreadMulticastListener(ComNode node) {
		this.node = node;
		yoloPack = new YoloPack();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			byte reply[] = node.receive(TIMEOUT);			
			try {
				data = new String(reply, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("data is being printed " + data);
			yoloPack.parsePackRx(data);

			System.out.println("Query type = " + yoloPack.getQuery());
			if (yoloPack.getQuery() == QueryType.LOGIN) {
				System.out.println("Setting login process");
				login_requested = true;
			} else if (yoloPack.getQuery() == QueryType.UPDATE_TABLE) {
				System.out.println("Updating table");
				setUpdate_table(true);
			} else if (yoloPack.getQuery() == QueryType.ARBITRATING) {
				System.out.println("Starting arbitration process");
				rx_inquire = true;
			} else if (yoloPack.getQuery() == QueryType.QUERY) {
				System.out.println("Verifying query");
				query_requested = true;
				
			}
		}

	}

	public boolean isRx_inquire() {
		return rx_inquire;
	}

	public void setRx_inquire(boolean rx_inquire) {
		this.rx_inquire = rx_inquire;
	}

	public boolean isLogin_requested() {
		return login_requested;
	}

	public void setLogin_requested(boolean login_requested) {
		this.login_requested = login_requested;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isUpdate_table() {
		return update_table;
	}

	public void setUpdate_table(boolean update_table) {
		this.update_table = update_table;
	}

	public boolean isQuery_answer() {
		return query_answer;
	}

	public void setQuery_answer(boolean query_answer) {
		this.query_answer = query_answer;
	}

	public boolean isQuery_requested() {
		return query_requested;
	}

	public void setQuery_requested(boolean query_requested) {
		this.query_requested = query_requested;
	}
}
