/**
 * Provides an interface for sending a discovery message.
 * The node which sends the discovery message doesn't need to know what nodes to send the message to.
 * The Discovery Protocol is the only class which knows which nodes should be able to receive a discovery message
 * 
 * This protocol forwards the discovery message on to nodes within range
 */
package main;

import java.util.HashMap;
import java.util.Map;

import message.Discover;
import node.Node;
import node.NodeThread;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class NeighbourDiscoveryProtocol {
	private Map<Integer, Node> nodes;
	private static NeighbourDiscoveryProtocol instance;

	protected NeighbourDiscoveryProtocol(Map<Integer, Node> nodes){
		this.nodes = nodes;
		instance = this;
	}
	
	/**
	 * 
	 * @return The Discovery Protocol, used to send Discovery messages within the network
	 */
	public static NeighbourDiscoveryProtocol getInstance(){
		if (instance != null){
			return instance;
		} else {
			throw new NullPointerException("No instance of NeighbourDiscoveryProtocol");
		}
	}
	
	
	/**
	 * When invoked a message is sent to all near by nodes requesting an acknowledgement
	 * @param message
	 */
	public void sendDiscoveryMessage(Discover message){
		// This is the node which has sent the discovery message and is waiting on responses from its neighbours
		Node nodeToSendDiscovery = nodes.get(message.getSentFromNode().getNodeID());
		// These are the nodes which the discovery message will go to and thus respond to
		Map<NodeThread, Double> nodesToReceiveResponseFrom = getNodesInRange(nodeToSendDiscovery);
		
		// Send the discovery message
		for (NodeThread node : nodesToReceiveResponseFrom.keySet()){
			message.setDistance(nodesToReceiveResponseFrom.get(node));
			node.addMessageToNodeQueue(message);
		}
	}
	
	/**
	 * Get all nodes which can be sent a message from the specified node
	 * @param node The node which needs to find all other nodes within range
	 * @return All the nodes which are in range and their corresponding distance
	 */
	private Map<NodeThread, Double> getNodesInRange(Node node){

		Map<NodeThread, Double> inRangeNodes = new HashMap<NodeThread, Double>();
		
		// Node A - the node to send the message
		// Node B - another node in the network
		// For all node Bs in the network calculate the distance between node A and node B.
		// If the distance is less than the node A's radius of communication then node B
		// can receive discovery messages from node A.
		for (Node otherNode : nodes.values()){
			if (otherNode.getNodeID() != node.getNodeID()){
				double deltaX = Math.pow((otherNode.getPosition().x - node.getPosition().x), 2);
				double deltaY = Math.pow((otherNode.getPosition().y - node.getPosition().y), 2);
				double radius = node.getCommunicationRadius();
				
				Double distance = Math.sqrt(deltaX + deltaY);
				boolean withinRange = distance < radius;
				
				if (withinRange){
					inRangeNodes.put(otherNode.getNodeThread(), distance);
				}
			}
		}
		return inRangeNodes;
	}
}