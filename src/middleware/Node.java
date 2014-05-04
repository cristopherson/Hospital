package middleware;

/**
 *
 * @author Cristopherson Torres
 *
 */
public interface Node {

    /**
     * Access to the specified Multicast address to join to that groups and
     * listen petitions from it
     *
     * @param address
     * @return true if success, otherwise false
     */
    boolean access(String address);

    /**
     * Send a message to the Multicast where it is already registered
     * @param message
     */
    void send(String message);

    /**
     * Listen to any message that are received in the Multicast group
     * @param timeout How much time it is going to expect a message from the group-
     * if zero then it waits indefinitely
     * @return an array of bytes containing the received message
     */
    byte[] receive(int timeout);
}
