/**
 * The Network Routing Protocol is responsible for routing messages to other nodes in the network.
 * It is also responsible for synchronous behaviour in the network.  Once all nodes have sent their
 * messages to the Routing Protocol, the Routing Protocol forwards the messages on to their destination
 * informing all nodes to perform computation on the messages they've received.
 */
package internetLayer;

import java.util.Queue;

/**
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class NetworkRoutingProtocol implements Runnable {
	private static NetworkRoutingProtocol instance;

	private int nodesWhichHaveSentAllMessages;
	private int nodesWhichHaveProcessedAllMessages;
	private int nodesInTheNetwork;
	private Queue<RoutingPacket> incomingPackets;
	
	public boolean process;

	private boolean send;
	
	public NetworkRoutingProtocol(int nodesInTheNetwork) {
		instance = this;
		this.nodesInTheNetwork = nodesInTheNetwork;
		new Thread(this).start();
	}
	
	public static NetworkRoutingProtocol getInstance(){
		if (instance != null){
			return instance;
		} else {
			throw new NullPointerException("No instance of NetworkRoutingProtocol");
		}
		
	}
	
	@Override
	public void run() {
		while (true){
			
//			RoutingPacket packetToForward;
//			while ((packetToForward = incomingPackets.poll()) != null){
//				NodeInterface sendToNode = packetToForward.toNode;
//				MessageInterface messageToSend = packetToForward.message;
//				
//				sendToNode.getNodeThread().addMessageToNodeQueue(messageToSend);
//			}
			System.out.println("Nodes which have sent messages: " + nodesWhichHaveSentAllMessages);
			if ((nodesInTheNetwork == nodesWhichHaveSentAllMessages)){
				guardedNotify();
			}
			
			System.out.println("Nodes which have processed messages: " + nodesWhichHaveProcessedAllMessages);
			if (nodesInTheNetwork == nodesWhichHaveProcessedAllMessages){
				resetNodesWhichHaveProcessedAllMessages();
				process = false;
				send = true;
				synchronized (instance) {
					System.out.println();
					System.out.println("All nodes can now send!");
					System.out.println();
					notifyAll();
				}
			}


			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void finishedSendingMessages(){
//		System.out.println("Incremented nodesWhichHaveSentAllMessages");
		nodesWhichHaveSentAllMessages++;
	}
	
	public void finishedProcessingMessages(){
//		System.out.println("Incremented nodesWhichHaveProcessedAllMessages");
		nodesWhichHaveProcessedAllMessages++;
	}
	
	private synchronized void resetNodesWhichHaveSentAllMessages(){
		nodesWhichHaveSentAllMessages = 0;
	}
	
	private synchronized void resetNodesWhichHaveProcessedAllMessages(){
		nodesWhichHaveProcessedAllMessages = 0;
	}
	
	public void guardedProcess() throws InterruptedException{
		synchronized (this){
			while (!process){
				wait();
			}
		}
	}
	
	public void guardedSend() throws InterruptedException {
		synchronized (this) {
			while (!send){
				wait();
			}
		}
	}
	
	private void guardedNotify(){
		synchronized (this) {
			resetNodesWhichHaveSentAllMessages();
			send = false;
			process = true;
			System.out.println();
			System.out.println("All nodes can now process!");			
			System.out.println();
			notifyAll();
		}
	}
}