/**
 * The Base Station which has control over the whole network
 */
package main;

import java.util.Map;

import message.MessageInterface;
import message.Payload;
import node.MWOE;
import node.Node;
import node.NodeInterface;
import concreteMessage.Discovery;
import concreteMessage.IdentifyNewEdgeBroadcast;
import concreteMessage.LeaderDecisionBroadcast;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class BaseStation implements Runnable {
	
	private static BaseStation instance;
	
	private Map<Integer, Node> nodesInTheNetwork;
	private int numberOfComponents;
	private int nodeReadyForNextLevel;
	
	public BaseStation(Map<Integer, Node> nodes){
		BaseStation.instance = this;
		this.nodesInTheNetwork = nodes;
		
		// Start up all the nodes
		startNodes();
		
		// Start tracking the level
		Thread baseStationThread = new Thread(this);
		baseStationThread.start();
		
	}
	
	public static BaseStation getInstance(){
		if (instance != null){
			return instance;
		} else {
			throw new NullPointerException("BaseStation instance does not exist");
		}
	}

	private void startNodes(){
		for (Node n : nodesInTheNetwork.values()) {
			n.getNodeThread().start();
		}
	}
	
	public synchronized void contstrucMST() throws InterruptedException{
		initComponentID();
		instructDiscovery();
	}
	
	public void initComponentID(){
		System.out.println("Initialising component IDs");
		for (Node n : nodesInTheNetwork.values()){
			n.setComponentID(++numberOfComponents);
		}
	}


	public void instructDiscovery(){
		// Instruct each node to send a discover message
		for (Node n : nodesInTheNetwork.values()){
//			n.getNodeThread().start();
			MessageInterface discovery = new Discovery();
			discovery.send(n);
			System.out.println("Sent discovery message for node " + n.getNodeID());
		}
		
		// Retrieve the neighbour nodes for each node in the network
		for (Node n : nodesInTheNetwork.values()){
			for (NodeInterface v : n.getNeighbourNodes().keySet()){
				System.out.println("Node ID: " + n.getNodeID() + " Neighbour Node: " + v.getNodeID() + " ComponentID: " + n.getComponentID());
			}
		}
	}

	private void findMinimumWeightedEdge(){
		for (Node n : nodesInTheNetwork.values()){
			
			if (n.isLeader()){
				// This is basically a broadcast message that performs a convergecast message as its final step
				// The broadcast message has no payload (null)
				MessageInterface broadcastMessage = new IdentifyNewEdgeBroadcast(null);
				
				// Broadcast the message with the structure above
				broadcastMessage.send(n);
			}
		}
	}
	
	private void broadcastLeaderDecision(){
		for (Node n : nodesInTheNetwork.values()){
			// If you are the leader then broadcast your decision to the rest of your component
			if (n.isLeader()){
				Payload<MWOE> payload = new Payload<MWOE>(n.componentMWOE);
				MessageInterface leaderDecisionBroadcast = new LeaderDecisionBroadcast(payload);
				leaderDecisionBroadcast.send(n);
			}
		}
	}
	
	public void ready(Node node) throws InterruptedException{
		synchronized (instance) {
			nodeReadyForNextLevel++;
			wait();
			node.readyForNextLevel = false;
		}
	}
	
	@Override
	public void run() {
		while (true){
			System.out.println("nodeReadyForNextLevel: " + nodeReadyForNextLevel + " nodesInTheNetwork: " + nodesInTheNetwork.size());
			if (nodeReadyForNextLevel == nodesInTheNetwork.size()){
				findMinimumWeightedEdge();
				synchronized(instance){
					nodeReadyForNextLevel = 0;
					notifyAll();
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
