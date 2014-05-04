package middleware;

import com121.Server121;

import yolopacking.YoloPack;

public class ThreadTcpListener implements Runnable {

    private final Server121 server;
    private final int TIMEOUT = 0;
    private final YoloPack yoloPack;
    private String data;
    private final String address;
    private final int port;
    private boolean rxTable = false;
    private boolean answerQuery = false;

    public ThreadTcpListener(String address, int port) {
        yoloPack = new YoloPack();
        this.server = new Server121();
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
                rxTable = true;
            }
            if (yoloPack.getQuery() == QueryType.QUERY_ANSWER) {

                setAnswerQuery(true);
            }
        }

    }

    boolean isRxTable() {
        return rxTable;
    }

    public void setRxTable(boolean rxTable) {
        this.rxTable = rxTable;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isAnswerQuery() {
        return answerQuery;
    }

    public void setAnswerQuery(boolean answerQuery) {
        this.answerQuery = answerQuery;
    }

}
