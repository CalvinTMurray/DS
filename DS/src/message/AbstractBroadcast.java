/**
 * An abstract broadcast message which sends a message to all child nodes within the MST
 * 
 * Two methods can be implemented:
 * 1) The action to perform at each node before that node broadcasts the message to its child nodes
 * 2) The action to perform at the end of the broadcast (at the leaf nodes)
 */
package message;

import java.util.Collection;

import node.Node;
import node.NodeInterface;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 * @param <T>
 *
 */
public abstract class AbstractBroadcast<T> implements MessageInterface {

	protected Payload<T> payload;
	protected Node fromNode;
	
	public AbstractBroadcast(Payload<T> payload) {
		this.payload = payload;
	}
	
	@Override
	public void performAction(Node node) {

		// Perform an action on the node before we broadcast the message to the children
		performActionBeforeBroadcast(node, payload);
		
		// If there are child nodes then broadcast the message
		if (!node.getMstNeighbourNodes().isEmpty()){
			node.getNodeThread().addMessageToNextRoundQueue(this);
		} else {
			System.out.println("Node " + node.getNodeID() + " doesn't have any child nodes");
			performActionIfNoChildren(node, payload);
		}
		
	}
	
	@Override
	public void send(Node node) {
		
		this.fromNode = node;
		
		// Includes the parent node
		Collection<NodeInterface> childNodes  = node.getMstNeighbourNodes().values();
		
		// Remove the parent node
		childNodes.remove(node.getParentNode());
		
		if (!(childNodes.isEmpty())){
			
			for (NodeInterface n : childNodes){
				n.getNodeThread().addMessageToNodeQueue(this);
				System.out.println("Sent a broadcast message from " + node.getNodeID() + " to " + n.getNodeID());
			}
		} else {
			node.getNodeThread().addMessageToNodeQueue(this);
		}
		
	}

	/**
	 * Perform an action before the broadcast message is forwarded on to its children
	 * @param node the current node which is to perform the action
	 */
	public abstract void performActionBeforeBroadcast(Node node, Payload<T> payload);
	
	/**
	 * Perform this action on the nodes which have no children (leaf nodes)
	 * @param node the current node which is to perform the action
	 */
	public abstract void performActionIfNoChildren(Node node, Payload<T> payload);
}
