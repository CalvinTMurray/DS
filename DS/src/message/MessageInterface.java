/**
 * The Message Interface uses polymorphism to encapsulate other types of messages
 * Thus the Node Thread can process all types of messages which implement this interface
 * without knowing the underlying message implementation
 */
package message;

import node.Node;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public interface MessageInterface {
	/**
	 * When the message is received perform this action
	 * @param node the node which should perform this action (e.g. m.performAction(this))
	 */
	public void performAction(Node node);
	
	/**
	 * Send the message
	 * @param node the node which is sending the message
	 */
	public void send(Node node);
}
