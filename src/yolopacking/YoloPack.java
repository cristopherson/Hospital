package yolopacking;

import middleware.QueryType;

public class YoloPack {

    private String protocol;
    private String sender;
    private String receiver;
    private QueryType ruery;
    private String data;
    private String answer;

    public YoloPack() {
        protocol = "";
        sender = "";
        receiver = "";
        ruery = QueryType.NONE;
        data = "";
    }

    public String createPackTx(String txSender, String txReceiver,
            QueryType txQuery, String txData, String txAnswer) {
        String txPackage;
        /*
         * Frame YOLO protocol: YOLO:<Sender>:<Receiver>:<Query>:<Data>:<Answer>
         */
        txPackage = "YOLO:" + txSender + ":" + txReceiver + ":" + txQuery + ":"
                + txData + ":" + txAnswer;
        return txPackage;
    }

    public void parsePackRx(String rxMessage) {
        String splittedMessage[] = rxMessage.split(":");

        if (splittedMessage.length < 6) {
            System.err.println("Invalid message: " + rxMessage);
            return;
        }

        protocol = splittedMessage[0];
        sender = splittedMessage[1];
        receiver = splittedMessage[2];
        ruery = parseStringToQuery(splittedMessage[3]);
        data = splittedMessage[4];
        answer = splittedMessage[5];

    }

    public String getProtocol() {
        return protocol;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public QueryType getQuery() {
        return ruery;
    }

    public String getData() {
        return data;
    }

    public String getAnswer() {
        return answer;
    }

    public void clear() {
        protocol = "";
        sender = "";
        receiver = "";
        ruery = QueryType.NONE;
        data = "";
        answer = "";
    }

    public QueryType parseStringToQuery(String query) {
        for (QueryType type : QueryType.values()) {
            if (query.equals(type.toString())) {
                return type;
            }
        }

        return QueryType.NONE;
    }

}
