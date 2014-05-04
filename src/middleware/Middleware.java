package middleware;

import java.util.Iterator;
import java.util.LinkedList;

import psudoapp.Info;

import yolopacking.YoloPack;
import com121.Client121;

public class Middleware {

    private final MemberList memberList;
    private final ComNode node;
    private final String multicastIp = "230.0.0.1";
    private boolean owner;
    private String id;
    private String address;
    private String port;
    private final Info inf;

    private long currentTime;
    private long currentTimeZ1;
    private long timeStamp;
    private boolean ID_Lock;
    private boolean rx_table;
    private boolean rx_inquire;
    private boolean timeoutInitialCondition = true;
    private boolean arbitrationActive;
    private final int rxTableTimeout = 500;// miliseconds
    private final int rxInquireTiemout = 500; // miliseconds
    private final int queryTiemout = 3000; // miliseconds
    private final ThreadTcpListener tcpThread;
    private final ThreadMulticastListener multicastThread;
    private String logBuffer = "";

    public enum StateMachineStage {

        TX_LOGIN, TX_TABLE, RX_TABLE, RX_TABLE_TIMEOUT, TX_INQUIRE, RX_INQUIRE, RX_INQUIRE_TIMEOUT, OWNERSHIP, FINISH

    }

    public Middleware(String id, String address, String port) {
        memberList = new MemberList();
        node = new ComNode();
        inf = new Info();
        owner = false;
        this.id = id;
        this.address = address;
        this.port = port;
        node.access(multicastIp);

        tcpThread = new ThreadTcpListener(this.address,
                Integer.parseInt(this.port));
        multicastThread = new ThreadMulticastListener(node);

        Thread myTcpThread = new Thread(tcpThread);
        Thread myMulticastThread = new Thread(multicastThread);

        myTcpThread.start();
        myMulticastThread.start();
    }

    public boolean arbitrate() {
        YoloPack yoloPack = new YoloPack();

        String newOwner = memberList.inquireNewOwner();

        if (newOwner.equals("")) {
            return false;
        }

        String txSender = MemberList.createMemberListItem(id, address, port);
        System.out.println(id + ">> Arbitrating");
        String message = yoloPack.createPackTx(txSender, "MULTI",
                QueryType.ARBITRATING, "none", "none");
        node.send(message);

        System.out.println("New owner " + newOwner);

        if (!newOwner.split(";")[0].equals(id)) {
            return false;
        }

        owner = true;
        Client121 tcpNode = new Client121();
        String receiverHospitalName = memberList.getHospitalName();
        String receiverIpAddress = memberList.getIpAddress();
        String receiverPort = memberList.getPort();
        String txReceiver = MemberList.createMemberListItem(
                receiverHospitalName, receiverIpAddress, receiverPort);

        String flatTable = memberList.getFlatMemberList();
        message = yoloPack.createPackTx(txSender, txReceiver,
                QueryType.SET_TABLE, flatTable, "none");
        boolean tcpMessageSuccess = false;
        final int MAX_RETRIES = 3;
        int retries = 0;

        System.out.println("Sending message " + message);
        do {
            tcpMessageSuccess = tcpNode.send(message, receiverIpAddress,
                    Integer.parseInt(receiverPort));
        } while (retries < MAX_RETRIES && !tcpMessageSuccess);

        return true;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void Set_Informer_ID(int ident_numb) {
        if (!this.ID_Lock) {
            this.ID_Lock = true;
        }
    }

    public String LoginArbitrationStateMachine() {
        StateMachineStage inner_stage = StateMachineStage.TX_LOGIN;
        YoloPack yoloPack = new YoloPack();
        arbitrationActive = true;

        do {
            switch (inner_stage) {
                case TX_LOGIN:
                    System.out.println(id + ">> Login");
                    // At this point a message has to be sent as a login "request"
                    // Call here message Tx with name:ip address: port
                    String message = yoloPack.createPackTx(
                            MemberList.createMemberListItem(id, address, port),
                            "MULTI", QueryType.LOGIN, "none", "none");

                    System.out.println(id + ">> message = " + message);
                    node.send(message);
                    inner_stage = StateMachineStage.RX_TABLE;
                    break;
                case RX_TABLE:
                    rx_table = tcpThread.isRxTable();
                    if (true == rx_table) {
                        System.out.println(id + ">> Rx table");
                        // Store message string or send it as parameter to function
                        // below
                        // Create Table setFlatMemberList(String flatmemberlist);
                        // Nothing else to do we are now part of the netguork
                        String data = tcpThread.getData();

                        yoloPack.parsePackRx(data);
                        System.out.println(id + ">> table " + yoloPack.getData());
                        memberList.setFlatMemberList(yoloPack.getData());

                        inner_stage = StateMachineStage.FINISH;
                    } else {
                        inner_stage = StateMachineStage.RX_TABLE_TIMEOUT;
                    }
                    break;
                case RX_TABLE_TIMEOUT:
                    currentTime = System.currentTimeMillis();
                    if (timeoutInitialCondition) {
                        // This way the first substraction for timestamp wont be
                        // currentTime - 0,
                        // which would happen to be very large
                        timeoutInitialCondition = false;
                    } else {
                        timeStamp += currentTime - currentTimeZ1;
                    }
                    currentTimeZ1 = currentTime;

                    if (rxTableTimeout <= timeStamp) {
                        inner_stage = StateMachineStage.TX_INQUIRE;
                        currentTime = 0;
                        timeStamp = 0;
                        currentTimeZ1 = 0;
                        timeoutInitialCondition = true;
                        System.out.println(id + ">> Rx table timeout");
                    } else {
                        inner_stage = StateMachineStage.RX_TABLE;
                    }
                    break;
                case TX_INQUIRE:
                    System.out.println(id + ">> Tx inquire");

                    // At this point a message has to be sent as a arbitrate
                    // "request"
                    // Call here message Tx
                    String txSender = MemberList.createMemberListItem(id, address,
                            port);
                    message = yoloPack.createPackTx(txSender, "MULTI",
                            QueryType.UPDATE_TABLE, "none", "none");
                    node.send(message);
                    arbitrate();
                    inner_stage = StateMachineStage.RX_INQUIRE;
                    break;

                case RX_INQUIRE:
                    rx_inquire = multicastThread.isRxInquire();
                    if (true == rx_inquire) {
                        // Nothing else to do we are now synched
                        System.out.println(id + ">> Rx inquire");
                        inner_stage = StateMachineStage.RX_TABLE;
                        multicastThread.setRxInquire(false);
                    } else {
                        inner_stage = StateMachineStage.RX_INQUIRE_TIMEOUT;
                    }
                    break;
                case RX_INQUIRE_TIMEOUT:
                    currentTime = System.currentTimeMillis();
                    if (timeoutInitialCondition) {
                        // This way the first substraction for timestamp wont be
                        // currentTime - 0,
                        // which would happen to be very large
                        timeoutInitialCondition = false;
                    } else {
                        timeStamp += currentTime - currentTimeZ1;
                    }
                    currentTimeZ1 = currentTime;

                    if (rxInquireTiemout <= timeStamp) {
                        System.out.println(id + ">> Rx inquire timeout");
                        inner_stage = StateMachineStage.OWNERSHIP;
                        currentTime = 0;
                        timeStamp = 0;
                        currentTimeZ1 = 0;
                        timeoutInitialCondition = true;
                    } else {
                        inner_stage = StateMachineStage.RX_INQUIRE;
                    }
                    break;
                case OWNERSHIP:
                    System.out.println(id + ">> I am owner");
                    // Add yourself to the memberlist table since you are a loner
                    owner = true;
                    memberList.addLastMember(MemberList.createMemberListItem(id,
                            address, port));
                    inner_stage = StateMachineStage.FINISH;
                    break;
                case FINISH:
                    System.out.println(id + ">> Login process completed");
                    arbitrationActive = false;
                    break;
                default:
                    System.err.println(id + ">> You are dancing with Bertha");
                    arbitrationActive = false;
            }

        } while (arbitrationActive);

        return ("SUCCESS");
    }

    /* Receive sender (Hospital Name;IP Address; Port) */
    public String Login(String newMember) {
        String ans = "";
        String[] array;

        memberList.addLastMember(newMember);
        if (owner) {
            ans = memberList.getFlatMemberList();
        }

        return ans;
    }

    public void txMemberList() {
        // TODO Auto-generated method stub
        if (multicastThread.isLoginRequested()) {
            processLogin(multicastThread.getData());
            multicastThread.setLoginRequested(false);
        }
        if (multicastThread.isUpdateTable()) {
            YoloPack yoloPack = new YoloPack();
            yoloPack.parsePackRx(multicastThread.getData());
            System.out.println("Sender = " + yoloPack.getSender());
            if (!yoloPack.getSender().split(";")[0].equals(id)) {
                arbitrate();
            }
            multicastThread.setUpdateTable(false);
        }
        if (multicastThread.isQueryRequested()) {
            processQuery();
            multicastThread.setQueryRequested(false);
        }
    }

    private void processQuery() {
        YoloPack yoloPack = new YoloPack();
        String data = multicastThread.getData();
        yoloPack.parsePackRx(data);
        Client121 client = new Client121();
        String txSender = MemberList.createMemberListItem(id, address, port);
        String txReceiver = yoloPack.getSender();
        String txData = yoloPack.getData();
        String senderAddress = yoloPack.getSender().split(";")[1];
        String senderPort = yoloPack.getSender().split(";")[2];

        if (!txReceiver.split(";")[0].equals(id)) {
            String txAnswer = "";
            boolean replyQuery = true;

            String queryInformation[] = txData.split(",");

            if (queryInformation.length < 1) {
                txAnswer = "Invalid Query";
            } else if (queryInformation.length < 2) {
                txAnswer = id + " has " + queryInformation[0] + " = \n";
                txAnswer += inf.GetInfoFromKey(queryInformation[0]);
            } else {
                replyQuery = inf.FindDataFromKey(queryInformation[0],
                        queryInformation[1]);
                if (replyQuery) {
                    txAnswer = id + " has " + queryInformation[0] + " = " + queryInformation[1];
                }
            }

            if (replyQuery) {
                String message = yoloPack.createPackTx(txSender, txReceiver,
                        QueryType.QUERY_ANSWER, txData, txAnswer);

                System.out.println("Replying query " + message);
                client.send(message, senderAddress,
                        Integer.parseInt(senderPort));
            }
        }

    }

    private void processLogin(String data) {
        // TODO Auto-generated method stub
        YoloPack yoloPack = new YoloPack();
        yoloPack.parsePackRx(data);

        if (!yoloPack.getSender().split(";")[0].equals(id)) {
            memberList.addLastMember(yoloPack.getSender());

            if (owner) {
                yoloPack.clear();

                Client121 client = new Client121();
                String txSender = MemberList.createMemberListItem(id, address,
                        port);
                String txReceiver = memberList.getLastMember();
                String txData = memberList.getFlatMemberList();

                String message = yoloPack.createPackTx(txSender, txReceiver,
                        QueryType.SET_TABLE, txData, "none");

                client.send(message, memberList.getIpAddress(),
                        Integer.parseInt(memberList.getPort()));

            }
        }
        multicastThread.setLoginRequested(false);
    }

    public void query(String query) {
        String txSender = MemberList.createMemberListItem(id, address, port);
        double replyInitialTime = System.currentTimeMillis();
        double replyCurrentTime;

        int timeout = 0;

        YoloPack yoloPack = new YoloPack();
        String message = yoloPack.createPackTx(txSender, "MULTI",
                QueryType.QUERY, query, "none");
        node.send(message);

        LinkedList<String> messageList = new LinkedList<String>();

        while (timeout <= queryTiemout) {
            if (tcpThread.isAnswerQuery()) {
                String data = tcpThread.getData();
                yoloPack.parsePackRx(data);

                messageList.add(yoloPack.getAnswer());
                tcpThread.setAnswerQuery(false);
            }
            replyCurrentTime = System.currentTimeMillis();
            timeout = (int) (replyCurrentTime - replyInitialTime);
        }

        System.out.println(id + ">> Following replies were received");
        Iterator<String> iterator = messageList.iterator();

        while (iterator.hasNext()) {
            String data = iterator.next();
            logBuffer += data + "\n";
            System.out.println(data);
        }
    }

    /**
     * @return the logBuffer
     */
    public String getLogBuffer() {
        return logBuffer;
    }
    
    public void clearLogBuffer(){
        logBuffer = "";
    }
    
    public void setInfoFile(String filePath) {
        inf.setFilePath(filePath);
    }
}
