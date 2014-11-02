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

import internetLayer.NetworkRoutingProtocol;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import concreteMessage.IdentifyNewEdgeConvergecast;
import main.BaseStation;
import message.MessageInterface;
import message.Payload;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class NodeThread extends Thread {
	
	// This thread's node
	private Node node;
	private Queue<MessageInterface> receivedMessages;
	private Queue<MessageInterface> nextRoundMessages;
	
	public NodeThread(Node node) {
		this.node = node;
		receivedMessages = new ConcurrentLinkedQueue<MessageInterface>();
		nextRoundMessages = new ConcurrentLinkedQueue<MessageInterface>();
	}
	
	/**
	 * This is message processing facility
	 */
	@Override
	public void run(){
		
		// We need to take account of the energy levels!!!

		while (true){
			
			
			MessageInterface messageToSend;
			System.out.println("Sending messages from node " + node.getNodeID());
			while (( messageToSend = nextRoundMessages.poll()) != null){
				messageToSend.send(node);
			}

			NetworkRoutingProtocol.getInstance().finishedSendingMessages();
			try {
				NetworkRoutingProtocol.getInstance().guardedProcess();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
//			System.out.println("I'm about to start processing");
			
//			System.out.println("Thread for node " + node.getNodeID() + " is active!");
			System.out.println("Queue size for node " + node.getNodeID() + ": " + receivedMessages.size());
			
			MessageInterface messageToProcess;
//			System.out.println("Number of threads running: " + Thread.activeCount());
			while ((messageToProcess = receivedMessages.poll()) != null){
				messageToProcess.performAction(node);
			}
			
			if (node.hasParentNode() && node.getReceivedConvergecastMessageCount() == node.getMstNeighbourNodes().size()){
				sendToParent();
			}

			NetworkRoutingProtocol.getInstance().finishedProcessingMessages();
			try {
				NetworkRoutingProtocol.getInstance().guardedSend();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (node.readyForNextLevel){
				System.out.println("Node " + node.getNodeID() + " is ready for the next level");
				try {
					BaseStation.getInstance().ready(node);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendToParent(){

		// Reset the number of messages received from this node's children
		node.resetReceivedConvergecastMessageCount();
		
		// 1) Get the next minimum weighted edge of this node, and add it to the list of 
		// received payloads from its children
		Payload<MWOE> currentMWOE = new Payload<MWOE>(node.getLocalMWOE());
		node.addPayload(currentMWOE);
		
		// 2) Compare all the payloads that were received from this node's children
		// and select the payload with the minimum weighted edge payload

		// The payload which will be sent to the parent
		MWOE payload = null;

		for (Payload<?> p : node.getReceivedPayloads()){

			MWOE currentPayload = null;

			try {
				currentPayload = (MWOE) p.getData();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (payload == null){
				payload = currentPayload;
			} else if (currentPayload.getDistance() < payload.getDistance()) {
				payload = currentPayload;
			}
		}
		
		MessageInterface convergecastMessage = new IdentifyNewEdgeConvergecast(payload);
		convergecastMessage.send(node);
		
		// IMPORTANT!!! 
		// Clear the node's payloads so that another message can use this node's payloads
		node.clearReceivedPayloads();

		// Remove the parent node
		node.setParentNode(null);

	}
	
	/**
	 * Send a message from one node thread to another
	 * @param node the ID of the node you want to send the message to
	 * @param message the message which should be sent to the node
	 */
	private void sendMessage(MessageInterface message) {
	}
	
	/**
	 * Add a message to this node's queue waiting to be processed
	 * @param message
	 */
	public void addMessageToNodeQueue(MessageInterface message) {
		receivedMessages.add(message);
		// Process the message/messages
//		System.out.println("Number of thread running: " + Thread.activeCount());
	}
	
	/**
	 * Add a message to an outgoing queue.  These messages will be sent on the next round
	 * @param message
	 */
	public void addMessageToNextRoundQueue(MessageInterface message) {
//		System.out.println("Added message to node " + node.getNodeID() + "'s next round queue");
		nextRoundMessages.add(message);
	}
}
