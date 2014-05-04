package edu.uag.sistemasdistribuidos;

/**
 * 
 * @author uidu6098
 *
 */
public interface Node {
	/**
	 * 
	 * @param address
	 * @return
	 */
	boolean access(String address);
	
	/**
	 * 
	 * @param message
	 */
	void send(String message);

	/**
	 * 
	 * @param timeout
	 * @return
	 */
	byte[] receive(int timeout);
}
