/**
 * A response message triggered by a discovery message containing:
 * 1) the node which should be added as a neighbour
 * 2) the distance between the node which sent the discovery message and 1)
 */
package concreteMessage;

import message.MessageInterface;
import node.Node;
import node.NodeInterface;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class Repsonse implements MessageInterface {
	
	// The node to send the response message to
	private NodeInterface toNode;
	private Double distanceBetweenNodes;
	private Node fromNode;
	
	/**
	 * A Response message triggered by a discovery message
	 * @param toNode the node to send the response message to
	 * @param distance the distance between the node which received the discovery message and the node which will send the response message
	 */
	public Repsonse(NodeInterface toNode, Double distance) {
		this.toNode = toNode;
		this.distanceBetweenNodes = distance;
	}

	/**
	 * Add the node which sent the response message to the list of neighbours
	 */
	@Override
	public void performAction(Node node) {
		node.addNeighbourNode(fromNode, distanceBetweenNodes);
		
		// After a response has been processed start constructing the MST
//		MessageInterface broadcastMessage = new IdentifyNewEdgeBroadcast(null);
//		node.getNodeThread().addMessageToNextRoundQueue(broadcastMessage);
		
		System.out.println("Processed Response message for node " + node.getNodeID());
		node.readyForNextLevel = true;
	}
	
	@Override
	public void send(Node node) {
		System.out.println("Sent response message from " + node.getNodeID() + " to " + toNode.getNodeID());
		this.fromNode = node;
		toNode.getNodeThread().addMessageToNodeQueue(this);
	}
}
