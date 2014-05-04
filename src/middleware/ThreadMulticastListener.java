package middleware;

import java.io.UnsupportedEncodingException;

import yolopacking.YoloPack;

public class ThreadMulticastListener implements Runnable {
	private ComNode node;
	private final int TIMEOUT = 0;
	private String data;
	private YoloPack yoloPack;
	private boolean rxInquire;
	private boolean loginRequested;
	private boolean updateTable;
	private boolean queryRequested;
	private boolean queryAnswer;

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
				loginRequested = true;
			} else if (yoloPack.getQuery() == QueryType.UPDATE_TABLE) {
				System.out.println("Updating table");
				setUpdateTable(true);
			} else if (yoloPack.getQuery() == QueryType.ARBITRATING) {
				System.out.println("Starting arbitration process");
				rxInquire = true;
			} else if (yoloPack.getQuery() == QueryType.QUERY) {
				System.out.println("Verifying query");
				queryRequested = true;
				
			}
		}

	}

	public boolean isRxInquire() {
		return rxInquire;
	}

	public void setRxInquire(boolean rxInquire) {
		this.rxInquire = rxInquire;
	}

	public boolean isLoginRequested() {
		return loginRequested;
	}

	public void setLoginRequested(boolean loginRequested) {
		this.loginRequested = loginRequested;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isUpdateTable() {
		return updateTable;
	}

	public void setUpdateTable(boolean updateTable) {
		this.updateTable = updateTable;
	}

	public boolean isQueryAnswer() {
		return queryAnswer;
	}

	public void setQueryAnswer(boolean queryAnswer) {
		this.queryAnswer = queryAnswer;
	}

	public boolean isQueryRequested() {
		return queryRequested;
	}

	public void setQueryRequested(boolean queryRequested) {
		this.queryRequested = queryRequested;
	}
}
