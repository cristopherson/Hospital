package yolopacking;

import edu.uag.sistemasdistribuidos.QueryType;

public class YoloPack {
	private String Protocol;
	private String Sender;
	private String Receiver;
	private QueryType Query;
	private String Data;
	private String Answer;

	public YoloPack() {
		Protocol = "";
		Sender = "";
		Receiver = "";
		Query = QueryType.NONE;
		Data = "";
	}

	public String createPackTx(String txSender, String txReceiver,
			QueryType txQuery, String txData, String txAnswer) {
		String txPackage;
		/*
		 * Frame YOLO Protocol: YOLO:<Sender>:<Receiver>:<Query>:<Data>:<Answer>
		 */
		txPackage = "YOLO:" + txSender + ":" + txReceiver + ":" + txQuery + ":"
				+ txData + ":" + txAnswer;
		return txPackage;
	}

	public void parsePackRx(String RxMessage) {
		String splittedMessage[] = RxMessage.split(":");

		if (splittedMessage.length < 6) {
			System.err.println("Invalid message: " + RxMessage);
			return;
		}

		Protocol = splittedMessage[0];
		Sender = splittedMessage[1];
		Receiver = splittedMessage[2];
		Query = parseStringToQuery(splittedMessage[3]);
		Data = splittedMessage[4];
		Answer = splittedMessage[5];

	}

	public String getProtocol() {
		return Protocol;
	}

	public String getSender() {
		return Sender;
	}

	public String getReceiver() {
		return Receiver;
	}

	public QueryType getQuery() {
		return Query;
	}

	public String getData() {
		return Data;
	}

	public String getAnswer() {
		return Answer;
	}

	public void Clear() {
		Protocol = "";
		Sender = "";
		Receiver = "";
		Query = QueryType.NONE;
		Data = "";
		Answer = "";
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
