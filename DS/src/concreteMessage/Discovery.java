/**
 * A discovery message which can be sent between nodes within distance R
 */
package concreteMessage;

import main.NeighbourDiscoveryProtocol;
import message.MessageInterface;
import node.Node;
import node.NodeInterface;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class Discovery implements MessageInterface {

	// The node which sent the discovery message (used in the response, similar to an IP address)
	private NodeInterface fromNode;	
	// The distance between the fromNode and another node
	private double distanceBetweenNodes;
	
	/**
	 * Create a new discovery message
	 */
	public Discovery() {
	}
	
	/**
	 * The node which sent the discovery message
	 * @return
	 */
	public NodeInterface getSentFromNode(){
		return fromNode;
	}
	
	/**
	 * Set the distance between the two nodes
	 * @param distance
	 */
	public void setDistance(Double distance){
		distanceBetweenNodes = distance;
	}
	
	@Override
	public void performAction(Node node) {
		MessageInterface response = new Repsonse(fromNode, distanceBetweenNodes);
		node.getNodeThread().addMessageToNextRoundQueue(response);
		
		System.out.println("Processed Discovery message for node " + node.getNodeID());
//		response.send(node);
	}

	@Override
	public void send(Node node) {
		this.fromNode = node;
		// Send the discovery message to the NeighbourDiscoveryProtocol (which forwards 
		// the message to nodes that are within range)
		NeighbourDiscoveryProtocol.getInstance().sendDiscoveryMessage(this);
	}
	
	public void setFromNode(NodeInterface node){
		this.fromNode = node;
	}
}
