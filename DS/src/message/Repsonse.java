/**
 * A response message triggered by a discovery message containing:
 * 1) the node which should be added as a neighbour
 * 2) the distance between the node which sent the discovery message and 1)
 */
package message;

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
	}
	
	@Override
	public void send(Node node) {
		this.fromNode = node;
		toNode.getNodeThread().addMessageToNodeQueue(this);
	}
}
