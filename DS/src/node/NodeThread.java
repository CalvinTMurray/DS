/**
 * NodeThread simulates the communication services of a node.
 * It also provides an abstraction so that Node objects cannot be directly manipulated
 * 
 * NodeThread allows:
 * 1) messages to be sent from this node to other node threads
 * 2) messages to be processed
 * 
 */
package node;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import message.MessageInterface;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class NodeThread extends Thread {
	
	// This thread's node
	private Node node;
	private Queue<MessageInterface> receivedMessages;
	
	public NodeThread(Node node) {
		this.node = node;
		receivedMessages = new ConcurrentLinkedQueue<MessageInterface>();
	}
	
	/**
	 * This is message processing facility
	 */
	@Override
	public void run() {
		
		// Continue operating and processing messages if we have enough energy
		while (node.getEnergy() > node.getMinimumBudget()){
			
			MessageInterface message;

			while ((message = receivedMessages.poll()) != null){
				message.performAction(node);
				System.out.println("Performed action on node: " + node.getNodeID());
			}
		}
	}

	/**
	 * Send a message from one node thread to another
	 * @param node the ID of the node you want to send the message to
	 * @param message the message which should be sent to the node
	 */
	public void sendMessage(MessageInterface message){
		
	}
	
	/**
	 * Add a message to this node's queue waiting to be processed
	 * @param message
	 */
	public synchronized void addMessageToNodeQueue(MessageInterface message){
		receivedMessages.add(message);
	}
}
