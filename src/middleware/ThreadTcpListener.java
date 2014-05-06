package middleware;

import com121.Client121;
import com121.Server121;

import yolopacking.YoloPack;

public class ThreadTcpListener implements Runnable {

    private final Server121 server;
    private final int TIMEOUT = 0;
    private final YoloPack yoloPack;
    private String data;
    private final String address;
    private final int port;
    private volatile boolean rxTable = false;
    private volatile boolean answerQuery = false;
    private volatile boolean queryAck = false;

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

            System.out.println("Response = " + yoloPack.getQuery() + " id = "+ yoloPack.getSender());
            if (yoloPack.getQuery() == QueryType.QUERY_ACK) {
                setQueryAck(true);
            }
            if (yoloPack.getQuery() == QueryType.SET_TABLE) {
                rxTable = true;
            }
            if (yoloPack.getQuery() == QueryType.QUERY_ANSWER) {
                String message = yoloPack.createPackTx(yoloPack.getReceiver(), yoloPack.getSender(), QueryType.QUERY_ACK, "none", "none");
                Client121 client = new Client121();
                client.send(message, yoloPack.getSender().split(";")[1], Integer.parseInt(yoloPack.getSender().split(";")[2]));
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

    public boolean isQueryAck() {
        return queryAck;
    }

    public void setQueryAck(boolean queryAck) {
        this.queryAck = queryAck;
    }
}
